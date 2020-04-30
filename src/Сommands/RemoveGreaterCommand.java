package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;
import object.Organization;

public class RemoveGreaterCommand extends Commands {
    public RemoveGreaterCommand(){
        cmdName = "remove_greater";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {

        Organization organization = consoleManager.getOrganization();

        int initSize = collectionManager.getCsvCollection().size();
        collectionManager.removeGreater(organization);
        int afterSize = collectionManager.getCsvCollection().size();

        consoleManager.print("Было удалено " + (initSize - afterSize) + " элементов");
    }
}