package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

import java.io.Serializable;

public abstract class Commands implements Serializable {

    int argCount = 0;
    String[] args;
    String cmdName;
    String description;
    boolean needInput = false;
    Object inputData = null;

    public Commands(){}
    /**
     *
     * @param consoleManager управление консолью
     * @param collectionManager управление коллекцией
     */
    public abstract Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials);

    public Object getInput(ConsoleManager consoleManager){
        return null;
    }


    public int getArgCount(){ return argCount; }
    public String getCmdName() {
        return cmdName;
    }
    public String[] getArgs(){ return this.args; }
    public String getDescription() {
        return description;
    }
    public boolean getNeedInput(){ return needInput; }

    public void setArgs(String[] args){ this.args = args; }
    public void setInputData(Object inputData){ this.inputData = inputData; }

    public boolean isNumeric(String strNum) {
        if (strNum == null)
            return false;
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

