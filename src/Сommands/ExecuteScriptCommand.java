package Сommands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import exceptions.NoCommandException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.ConsoleManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecuteScriptCommand extends Commands {
    public ExecuteScriptCommand(){
        cmdName = "execute_script";
        argCount = 1;
    }
    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        return null;
    }
}

