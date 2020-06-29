//package ru.ifmo.lab.network;
//
//import ru.ifmo.lab.database.Credentials;
//import ru.ifmo.lab.database.CurrentUser;
//import ru.ifmo.lab.exceptions.InvalidValueException;
//import ru.ifmo.lab.exceptions.NoCommandException;
//import lombok.extern.slf4j.Slf4j;
//import ru.ifmo.lab.managers.CommandManager;
//import ru.ifmo.lab.managers.ConsoleManager;
//import ru.ifmo.lab.utilits.Const;
//
//
//import javax.security.sasl.AuthenticationException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.InetSocketAddress;
//import java.nio.channels.ClosedChannelException;
//import java.util.NoSuchElementException;
//
//@Slf4j
//public class ClientMain {
//
//    public static void main(String[] args) {
//        InetSocketAddress address = null;
//        ClientUdpChannel channel = null;
//        try {
//            if (args.length >= 2) {
//                address = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
//            } else if (args.length == 1) {
//                String[] hostAndPort = args[0].split(":");
//                if (hostAndPort.length != 2) {
//                    throw new InvalidValueException("");
//                }
//                address = new InetSocketAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
//            } else {
//                address = new InetSocketAddress("localhost", Const.DEFAULT_PORT);
//            }
//        }catch (Exception ex){
//            System.err.println(ex.getMessage());
//            System.exit(-1);
//        }
//
//        try {
//            channel = new ClientUdpChannel();
//        } catch (IOException ex) {
//            log.error("Unable to connect to the server", ex);
//            System.exit(-1);
//        }
//
//        CurrentUser currentUser = new CurrentUser(new Credentials(-1, "default", ""));
//        ConsoleManager consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(System.out), false);
//        CommandManager commandsManager = new CommandManager();
//        CommandReader commandReader = new CommandReader(channel, commandsManager, consoleManager);
//        ClientHandler clientHandler = new ClientHandler(channel, currentUser);
//
//        while (true){
//            try {
//                if (channel.isConnected())
//                    commandReader.startInteraction(currentUser.getCredentials());
//                else
//                    channel.tryToConnect(address);
//
//                clientHandler.checkForResponse();
//
//            } catch (NoCommandException | AuthenticationException ex) {
//                System.out.println(ex.getMessage());
//            } catch (NoSuchElementException ex) {
//                commandReader.finishClient();
//                clientHandler.finishReceiver();
//            } catch (ClosedChannelException ignored) {
//            }catch (ArrayIndexOutOfBoundsException ex) {
//                System.err.println("No argument passed");
//            } catch (IOException | ClassNotFoundException e) {
//                System.err.println("I/O Problems, check logs");
//                log.error("I/O Problems", e);
//            }
//        }
//    }
//}
