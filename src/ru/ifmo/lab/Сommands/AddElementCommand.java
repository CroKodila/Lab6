package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;
import ru.ifmo.lab.object.Organization;

public class AddElementCommand extends Commands {
    public AddElementCommand (){
      cmdName = "add";
        needInput = true;
    };
    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getOrganization();
    }
    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        String organizationID = databaseController.addOrganizaton((Organization) inputData, credentials);
        if (isNumeric(organizationID)) {
            ((Organization) inputData).setId(Long.valueOf(organizationID));
            ((Organization) inputData).setUserID(credentials.id);
            ((Organization) inputData).setUsername(credentials.username);
            collectionManager.add((Organization) inputData);
            consoleManager.print("Element is added");
        }

        inputData = null;

        return true;
    }
}

