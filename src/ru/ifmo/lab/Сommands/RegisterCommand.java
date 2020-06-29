package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class RegisterCommand extends Commands {

    public RegisterCommand(){
        cmdName = "register";
        description = "регистрация пользователя в систему, для управления им его данными";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCredentials();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        Object _credentials = databaseController.register((Credentials) this.inputData);
        inputData = null;
        if (_credentials instanceof Credentials) {
            return _credentials;
        } else {
            consoleManager.print((String) _credentials);
            return false;
        }
    }
}
