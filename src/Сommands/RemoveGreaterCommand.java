package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;
import object.Organization;

public class RemoveGreaterCommand extends Commands {
    public RemoveGreaterCommand(){
        cmdName = "remove_greater";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getOrganization();
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);


        int initSize = collectionManager.getCsvCollection().size();
        collectionManager.removeGreater((Organization)inputData);
        int afterSize = collectionManager.getCsvCollection().size();

        consoleManager.print("Было удалено " + (initSize - afterSize) + " элементов");
    }
}