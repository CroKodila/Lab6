package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

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

        return true;
    }
}
