package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class InfoCommand extends Commands {
    public InfoCommand(){
        cmdName = "info";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        consoleManager.print("Type: " + collectionManager.getCollectionType());
        consoleManager.print("Size: "+ collectionManager.size());
        consoleManager.print("Initialization date: "+ collectionManager.getInitDate());
    }
}