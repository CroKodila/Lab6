package network;

import lombok.extern.slf4j.Slf4j;

import Сommands.Commands;
import Сommands.ExecuteScriptCommand;
import Сommands.ExitCommand;
import Сommands.SaveCommand;
import exceptions.InvalidValueException;
import exceptions.NoCommandException;
import exceptions.RecursiveExeption;
import managers.CommandManager;
import managers.ConsoleManager;
import network.packs.*;
import utilits.Const;
import utilits.Serializer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

@Slf4j
public class Client {
    private HashSet<String> executePaths = new HashSet<>();
    private boolean executeFault = false;

    private DatagramSocket socket;
    private InetAddress IPAddress;
    private int PORT;

    private ConsoleManager consoleManager;
    private boolean isConnected = false;
    private boolean isLogin = false;
    private int tryConnect = 5;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        new Client(args).run();
    }

    public Client(String[] args) throws IOException {
        connect(args);
    }

    private void connect(String host, int port) throws IOException {
        PORT = port;
        IPAddress = InetAddress.getByName(host);
        socket = new DatagramSocket();
        socket.setSoTimeout(5000);
        isConnected = true;
    }

    private void connect(String[] args) throws IOException {
        try {
            if (args.length >= 2) {
                connect(args[0], Integer.parseInt(args[1]));
            } else if (args.length == 1) {
                String[] hostAndPort = args[0].split(":");
                if (hostAndPort.length != 2) {
                    throw new InvalidValueException("");
                }
                connect(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            } else {
                connect("localhost", Const.DEFAULT_PORT);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void run() throws IOException, ClassNotFoundException, InterruptedException {
        consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(System.out), false);

        while (!isLogin) {
            send(new LoginPack());
            consoleManager.print("Ожидание подключения");
            objectHandler(recv());

        }

        tryConnect = 1;
        while (isConnected) {

            if (consoleManager.hasNextLine()) {
                executePaths.clear();
                executeFault = false;
                sendCommand(consoleManager.read(), consoleManager);
            }
        }
    }

    private void sendCommand(String sCmd, ConsoleManager cMgr) throws IOException, ClassNotFoundException {
        if (sCmd.isEmpty()) return;
        try {
            Commands cmd = CommandManager.getInstance().parseCommand(sCmd);
            if (cmd instanceof ExitCommand) {
                socket.disconnect();
                isConnected = false;
            } else if (cmd instanceof SaveCommand) {
                cMgr.print("Такая команда не доступна клиенту");
            } else if (cmd instanceof ExecuteScriptCommand) {

                if (executePaths.contains(cmd.getArgs()[0])) {
                    executeFault = true;
                    throw new RecursiveExeption("Рекурсивный вызов запрещен");
                }

                Path pathToScript = Paths.get(cmd.getArgs()[0]);
                executePaths.add(cmd.getArgs()[0]);
                int lineNum = 1;
                try {
                    cMgr = new ConsoleManager(new FileReader(pathToScript.toFile()), new OutputStreamWriter(System.out), true);
                    for (lineNum = 1; cMgr.hasNextLine(); lineNum++) {
                        String line = cMgr.read().trim();
                        if (!line.isEmpty() && !executeFault) {
                            sendCommand(line, cMgr);
                        }
                    }
                } catch (FileNotFoundException e) {
                    consoleManager.print("Файл не найден.");
                } catch (RecursiveExeption ex) {
                    consoleManager.print(ex.getMessage());
                } catch (StackOverflowError | NullPointerException ex) {
                    consoleManager.print("Выполнение прервано!");
                    return;
                }

            } else {
                if (cmd.getNeedInput()) cmd.setInputData(cmd.getInput(consoleManager));
                send(cmd);
                objectHandler(recv());
            }
        } catch (NoCommandException ex) {
            cMgr.print("Такая команда не найдена :(\nВведите команду help, чтобы вывести спискок команд");
            log.error(ex.getMessage());
        } catch (NumberFormatException | ClassCastException ex) {
            cMgr.print("Ошибка\n" + ex.getMessage());
            log.error(ex.getMessage());
        } catch (InvalidValueException ex) {
            cMgr.print(ex.getMessage());
            log.error(ex.getMessage());
        }
    }


    private void objectHandler(Object obj) {
        if (obj != null) {

            if (obj instanceof CommandExecPack) {
                consoleManager.print(((CommandExecPack) obj).getMessage());
            }else if (obj instanceof LoginSuccessfulPack){
                isConnected = true;
                isLogin = true;
                consoleManager.print("Подключено:) U r welcome)\nВведите команду help, чтобы вывести спискок команд");
            }

        }
    }

    private void send(Object obj) throws IOException {

        byte[] data = Serializer.Serialize(obj);
        final ByteBuffer buf = ByteBuffer.wrap(data);

        try {
            DatagramPacket packet = new DatagramPacket(buf.array(), buf.array().length, IPAddress, PORT);
            socket.send(packet);
        } catch (IOException ex) {
            consoleManager.print(ex.getMessage());
        }

    }

    private Object recv() throws ClassNotFoundException, IOException {
        Object out = null;
        try {
            byte[] receiveData = new byte[Const.MESSAGE_BUFFER];
            DatagramPacket received = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(received);
            if (received.getLength() != 0) {
                out = Serializer.Deserialize(received.getData());
            }

        } catch (SocketTimeoutException ex) {
            if (tryConnect == 0) System.exit(1);

            tryConnect--;
            consoleManager.print("Ошибка подключения");
        }

        return out;
    }
}
