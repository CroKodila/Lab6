package Сommands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;
import object.Organization;

public class UpdateIDCommand extends Commands {
    public UpdateIDCommand(){
        cmdName = "update";
        argCount = 1;
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getOrganization();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            throw new InvalidValueException("Format error");
        }

        String cityID = databaseController.updateOrganization(id, (Organization) inputData, credentials);
        if (cityID == null) {
            if(collectionManager.update((Organization) inputData, (long)id))
                consoleManager.print("Element with id(" + id + ") - edited");
            else
                consoleManager.print("Element with id(" + id + ") - doesn't");
        } else {
            consoleManager.print("Have some problems: " + cityID);
        }

        inputData = null;

        return null;
    }

}
