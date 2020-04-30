package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public abstract class Commands {

    int argCount = 0;
    String cmdName;
    String description;

    /**
     *
     * @param consoleManager управление консолью
     * @param collectionManager управление коллекцией
     * @param args аргументы, которые ввел пользователь в консоле
     */
    public abstract void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args);


    public String getCmdName() {
        return cmdName;
    }

    public String getDescription() {
        return description;
    }
}

