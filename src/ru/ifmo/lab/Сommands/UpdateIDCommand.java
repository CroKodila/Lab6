package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.exceptions.InvalidValueException;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;
import ru.ifmo.lab.object.Organization;

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
            if (collectionManager.update((Organization) inputData, (long) id)) {
                consoleManager.print("Element with id(" + id + ") - edited");

                inputData = null;
                return true;
            } else {
                consoleManager.print("Element with id(" + id + ") - doesn't");

                inputData = null;
                return false;
            }

        } else {
            consoleManager.print("Have some problems: " + cityID);

            inputData = null;
            return false;
        }
    }
}
