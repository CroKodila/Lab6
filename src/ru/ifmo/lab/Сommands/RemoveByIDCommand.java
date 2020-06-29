package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.exceptions.InvalidValueException;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class RemoveByIDCommand extends Commands {
    public RemoveByIDCommand(){
        cmdName = "remove_by_id";
        argCount = 1;
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            throw new InvalidValueException(e.getMessage());
        }

        String cityID = databaseController.removeOrganization(id, credentials);
        if (cityID == null) {
            if(collectionManager.removebyID(id)) {consoleManager.print("Element with id(" + id + ") - successfully deleted"); return true;}
            else {consoleManager.print("Element with id(" + id + ") - doesn't exist"); return false;}
        } else {
            consoleManager.print("Have some problems: " + cityID);
            return false;
        }

    }
}
