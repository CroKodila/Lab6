package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class InfoCommand extends Commands {
    public InfoCommand(){
        cmdName = "info";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        consoleManager.print("Type: " + collectionManager.getCollectionType());
        consoleManager.print("Size: "+ collectionManager.size());
        consoleManager.print("Initialization date: "+ collectionManager.getInitDate());
    }
}