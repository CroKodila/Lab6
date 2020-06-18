package Сommands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class InfoCommand extends Commands {
    public InfoCommand(){
        cmdName = "info";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        consoleManager.print("Type: " + collectionManager.getCollectionType());
        consoleManager.print("Size: "+ collectionManager.size());
        consoleManager.print("Initialization date: "+ collectionManager.getInitDate());
        return null;
    }
}