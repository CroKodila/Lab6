package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;
import object.Organization;

public class RemoveLowerCommand extends Commands {
    public RemoveLowerCommand(){
        cmdName = "remove_lower";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {

        Organization organization = consoleManager.getOrganization();

        int initSize = collectionManager.getCsvCollection().size();
        collectionManager.removeLower(organization);
        int afterSize = collectionManager.getCsvCollection().size();

        consoleManager.print("Было удалено " + (initSize - afterSize) + " элементов");
    }
}
