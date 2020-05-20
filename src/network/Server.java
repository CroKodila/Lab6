package network;

import Сommands.Commands;
import exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import managers.CollectionManager;

import managers.ConsoleManager;
import network.packs.*;
import utilits.Const;
import utilits.Serializer;


import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class Server {

    class Con {
        ByteBuffer req;
        ByteBuffer resp;
        SocketAddress sa;

        public Con() {
            req = ByteBuffer.allocate(Const.MESSAGE_BUFFER);
        }
    }


    private boolean isRunning = false;
    private DatagramChannel channel;
    private Map<String, SocketAddress> clients = new HashMap<String, SocketAddress>();
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private Selector selector = Selector.open();

    private CollectionManager collectionManager;
    private ConsoleManager consoleManager;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Server(args).run();
    }


    public Server(String[] args) throws IOException {
        try {
            if (args.length == 1) {
                startServer(Integer.parseInt(args[0]));
            } else {
                startServer(Const.DEFAULT_PORT);
            }
        } catch (SocketException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Thread.sleep(200);
                System.out.println("Прерывание...");

                this.stopServer();
                channel.disconnect();
                collectionManager.save();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private void startServer(int port) throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port));

        collectionManager = new CollectionManager(Const.FILE_PATH);
        consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(outputStream), false);

        log.info("Сервер запущен. Порт: {}", port);
    }

    private void stopServer() {
        isRunning = false;
    }

    public void run() throws IOException, ClassNotFoundException {
        isRunning = true;

        log.info("Готов к получению данных");

        SelectionKey clientKey = channel.register(selector, SelectionKey.OP_READ);
        clientKey.attach(new Con());


        while (isRunning) {
            try {
                selector.select();
                Iterator selectedKeys = selector.selectedKeys().iterator();


                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();
                    selectedKeys.remove();

                    if (! key.isValid()) {
                        continue;
                    }

                    if (key.isReadable()) {
                        read(key);
                        key.interestOps(SelectionKey.OP_WRITE);
                    }

                    if (key.isWritable()) {
                        write(key);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }

            } catch (IOException e) {
                log.error("IO Exception... " + (e.getMessage() != null ? e.getMessage() : ""));
            }
        }
    }

    private void read(SelectionKey key) throws IOException, ClassNotFoundException {
        DatagramChannel chan = (DatagramChannel) key.channel();
        Con con = (Con) key.attachment();
        con.sa = chan.receive(con.req);
        con.req.flip();

        int limits = con.req.limit();
        byte bytes[] = new byte[limits];
        con.req.get(bytes, 0, limits);

        Object obj = Serializer.Deserialize(bytes);
        con.req.clear();

        con.resp = ByteBuffer.wrap(Serializer.Serialize(objectHandler(obj, con.sa)));
    }

    private void write(SelectionKey key) throws IOException {
        DatagramChannel chan = (DatagramChannel) key.channel();
        Con con = (Con) key.attachment();
        chan.send(con.resp, con.sa);
    }


    private Object objectHandler(Object obj, SocketAddress client) throws IOException {
        Object outObj = null;

        if (obj instanceof LoginPack) {
            outObj = new LoginSuccessfulPack();
            log.info("Пользователь подключен " + client);


        } else if (obj instanceof Commands) {
            outputStream.reset();
            try {
                ((Commands) obj).execute(consoleManager, collectionManager);
                outObj = new CommandExecPack(new String(outputStream.toByteArray()));
            } catch (InvalidValueException ex) {
                outObj = new CommandExecPack(ex.getMessage());
            }
            log.info(obj.toString());
            log.info(client.toString());
        }

        return outObj;
    }
}
