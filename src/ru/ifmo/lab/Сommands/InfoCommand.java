package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class InfoCommand extends Commands {
    public InfoCommand(){
        cmdName = "info";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        consoleManager.print("Type: " + collectionManager.getCollectionType());
        consoleManager.print("Size: "+ collectionManager.size());
        consoleManager.print("Initialization date: "+ collectionManager.getInitDate());
        return true;
    }
}