package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class ExitCommand extends Commands {
    public ExitCommand(){
        cmdName = "exit";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        if(consoleManager.getIsScript()) System.exit(1);
        while(true) {
            String out = consoleManager.readmessage("Вы уверены? Данные не сохраняются (Y/N)", false);
            if (out.toLowerCase().equals("y")) {
                System.exit(1);
            };
            if (out.toLowerCase().equals("n")) {
                 break;
            };
        }
    }
}
