package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class ShowCommand extends Commands {
    public ShowCommand(){
        cmdName = "show";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        consoleManager.print("Количество элементов в коллекции: " + collectionManager.getCsvCollection().size());
        StringBuilder sb = new StringBuilder();
        collectionManager.getCsvCollection().forEach(s -> sb.append(s).append("\n"));

        consoleManager.print(sb.toString());
    }
}
