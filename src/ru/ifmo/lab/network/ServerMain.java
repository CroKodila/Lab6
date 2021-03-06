package ru.ifmo.lab.network;

import ru.ifmo.lab.database.CollectionDBManager;
import ru.ifmo.lab.database.DBConfigure;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.database.UserDBManager;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.utilits.Const;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.sql.SQLException;

@Slf4j
public class ServerMain {

    public static void main(String[] args) {

        InetSocketAddress address = null;
        ServerSocket socket = null;
        try {
            if (args.length == 1) {
                address = new InetSocketAddress(Integer.parseInt(args[0]));
            }else{
                address = new InetSocketAddress(Const.DEFAULT_PORT);
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            System.exit(-1);
        }

        try {
            socket = new ServerSocket(address);

            final DBConfigure dbConfigure = new DBConfigure();
            dbConfigure.connect();

            final CollectionDBManager collectionDBManager = new CollectionDBManager(dbConfigure.getDbConnection());
            final UserDBManager userDBManager = new UserDBManager(dbConfigure.getDbConnection());
            final DatabaseController controller = new DatabaseController(collectionDBManager, userDBManager);

            final CollectionManager collectionManager = new CollectionManager(controller.getCollectionFromDB());

            final ServerHandler serverHandler = new ServerHandler(socket, collectionManager, controller);

            if (socket.getSocket().isBound()) {
                log.info("Socket Successfully opened on " + address);
            }
            else {
                log.error("Strange behaviour trying to bind the server");
                System.exit(-1);
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                serverHandler.disconnect();
                dbConfigure.disconnect();
            }));

            serverHandler.receiveFromWherever();

            while (socket.getSocket().isBound()) {
            }

        } catch (SQLException | SocketException e) {
            e.printStackTrace();
        }
    }

}
