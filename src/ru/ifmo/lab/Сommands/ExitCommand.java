package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class ExitCommand extends Commands {
    public ExitCommand() {
        cmdName = "exit";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        return null;
    }
}