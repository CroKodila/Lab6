package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class ShowCommand extends Commands {
    public ShowCommand(){
        cmdName = "show";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        consoleManager.print("Количество элементов в коллекции: " + collectionManager.getCsvCollection().size());
        StringBuilder sb = new StringBuilder();
        collectionManager.getCsvCollection().forEach(s -> sb.append(s).append("\n"));

        consoleManager.print(sb.toString());
    }
}
