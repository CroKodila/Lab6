package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class ClearCommand extends Commands {
    public ClearCommand(){
        cmdName = "clear";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        collectionManager.clear();
        consoleManager.print(" Все данные удалены");
    }
}
