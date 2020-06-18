package Сommands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;

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
            if(collectionManager.removebyID(id)) consoleManager.print("Element with id(" + id + ") - successfully deleted");
            else consoleManager.print("Element with id(" + id + ") - doesn't exist");
        } else {
            consoleManager.print("Have some problems: " + cityID);
        }

        return null;
    }
}
