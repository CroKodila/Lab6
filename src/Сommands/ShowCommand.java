package Сommands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class ShowCommand extends Commands {
    public ShowCommand(){
        cmdName = "show";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        consoleManager.print("Count: " + collectionManager.getCsvCollection().size());
        StringBuilder sb = new StringBuilder();
        collectionManager.getCsvCollection().forEach(s -> sb.append(s).append("\n"));

        consoleManager.print(sb.toString());

        return null;
    }

}
