package Сommands;

import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;
import object.Organization;

public class UpdateIDCommand extends Commands {
    public UpdateIDCommand(){
        cmdName = "update";
        argCount = 1;
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getOrganization();
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        if (args.length < 1) {
            throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + argCount);
        }

        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return;
        }

        if(!collectionManager.checkIdExist(id))
            throw new InvalidValueException("Такого id не существует");
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        collectionManager.update((Organization)inputData, id);
        consoleManager.print("Элемент с id - " + id + " был изменен");
    }

}
