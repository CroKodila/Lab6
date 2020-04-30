package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class ClearCommand extends Commands {
    public ClearCommand(){
        cmdName = "clear";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        collectionManager.clear();
        consoleManager.print(" Все данные удалены");
    }
}
