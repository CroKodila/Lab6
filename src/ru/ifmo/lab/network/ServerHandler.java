package ru.ifmo.lab.network;


import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;
import ru.ifmo.lab.network.packs.CommandExecPack;
import ru.ifmo.lab.network.packs.CommandPack;
import ru.ifmo.lab.utilits.Const;
import ru.ifmo.lab.Сommands.*;


import java.io.*;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ServerHandler {

    private class RequestReceiver extends Thread {

        @Override
        public void run() {
            while (true) {
                receiveData();
            }
        }

        /**
         * Функция для получения данных
         */
        public void receiveData() {
            SocketAddress addressFromClient = null;
            try {
                final ByteBuffer buf = ByteBuffer.allocate(Const.MESSAGE_BUFFER);
                addressFromClient = socket.receiveDatagram(buf);
                buf.flip();
                final byte[] petitionBytes = new byte[buf.remaining()];
                buf.get(petitionBytes);

                if (petitionBytes.length > 0)
                    processRequest(petitionBytes, addressFromClient);

            } catch (SocketTimeoutException ignored) {
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Weird errors, check log");
                log.error("Weird errors processing the received data", e);
                executeObj("Weird errors, check log. " + e.getMessage(), addressFromClient);
            }
        }

        /**
         * Функция для десериализации данных
         * @param petitionBytes - полученные данные
         */
        private void processRequest(byte[] petitionBytes, SocketAddress addressFromClient) throws IOException, ClassNotFoundException {
            try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(petitionBytes))) {
                final Object obj = stream.readObject();
                log.info("received object: " + obj);
                if (obj == null)
                    throw new ClassNotFoundException();
                executeObj(obj, addressFromClient);
            }
        }
    }

    private final Set<SocketAddress> clientList = new HashSet<>();
    private final ServerSocket socket;
    private final RequestReceiver requestReceiver;
    private final ForkJoinPool executor;
    private final ConsoleManager consoleManager;
    private final CollectionManager collectionManager;
    private final DatabaseController databaseController;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public ServerHandler(ServerSocket socket, CollectionManager collectionManager, DatabaseController databaseController) {
        this.socket = socket;
        this.consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(outputStream), false);
        this.collectionManager = collectionManager;
        this.databaseController = databaseController;

        requestReceiver = new RequestReceiver();
        requestReceiver.setName("ServerReceiverThread");
        executor = new ForkJoinPool();
    }

    public void receiveFromWherever() {
        requestReceiver.start();
    }

    private void executeObj(Object obj, SocketAddress addressFromClient) {
        Future<Object> resulted = executor.submit(() -> {
            Object responseExecution;
            clientList.add(addressFromClient);
            if (obj instanceof String)
                responseExecution = obj;
            else {
                Commands command = ((CommandPack) obj).getCommand();
                Credentials credentials = ((CommandPack) obj).getCredentials();
                String messageType = command.getClass().getSimpleName();
                try {
                    outputStream.reset();
                    Object retObj = command.execute(consoleManager, collectionManager, databaseController, credentials);
                    if(retObj instanceof Credentials || command instanceof ShowCommand){
                        responseExecution = new CommandExecPack(messageType, retObj, true);
                    }else if(retObj instanceof Boolean){
                        responseExecution = new CommandExecPack(messageType, new String(outputStream.toByteArray()), (Boolean) retObj);
                        if(command instanceof ClearCommand ||command instanceof RemoveByIDCommand || command instanceof UpdateIDCommand || command instanceof AddElementCommand)
                            new Thread(() -> { clientList.forEach(address -> executeObj(new CommandPack(new ShowCommand(), credentials), address)); }).start();
                    }else {
                        responseExecution = new CommandExecPack(messageType, new String(outputStream.toByteArray()), true);
                    }
                } catch (InvalidValueException ex) {
                    responseExecution = ex.getMessage();
                    log.error(ex.getMessage());
                }
            }
            socket.sendResponse(responseExecution, addressFromClient);
            return responseExecution;
        });

        try {
            System.out.println("Object gotten from executor: \n" + resulted.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error getting result from executor", e);
            System.out.println("Error getting result from executor: " + e.getMessage());
        }
    }


    public void disconnect() {
        log.info("Disconnecting the server...");
        System.out.println("Disconnecting the server...");
        try {
            executor.shutdown();
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupted executor during shutdown",e);
            System.out.println("Interrupted during finishing the queued tasks");
        }
        socket.getSocket().disconnect();
        requestReceiver.interrupt();
    }
}
