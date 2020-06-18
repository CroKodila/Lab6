package Сommands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class ClearCommand extends Commands {
    public ClearCommand(){
        cmdName = "clear";
    }
    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        String retDelAll = databaseController.clearOrganization(credentials);
        if (retDelAll == null) {
            collectionManager.clear();
            consoleManager.print("All elements deleted");
        }else{
            consoleManager.print("Problem: " + retDelAll);
        }

        return null;
    }
}
