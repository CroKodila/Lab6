package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class ExitCommand extends Commands {
    public ExitCommand(){
        cmdName = "exit";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        System.exit(1);
    }
}
