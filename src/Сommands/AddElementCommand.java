package Сommands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;
import object.Organization;

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
            collectionManager.add((Organization) inputData);
            consoleManager.print("Element is added");
        }

        inputData = null;

        return null;
    }
}

