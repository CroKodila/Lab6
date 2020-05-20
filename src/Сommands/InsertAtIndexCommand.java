package Сommands;

import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;
import object.Organization;

public class InsertAtIndexCommand extends Commands {
    public InsertAtIndexCommand() {
        cmdName = "insert_at";
        argCount = 1;
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getOrganization();
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        if (args.length < argCount) {
            throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + argCount);
        }
        long id;
        try {
            id = Long.parseLong(args[0]);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return;
        }
        if (!collectionManager.checkIdExist(id)) {
            if(needInput && inputData == null) inputData = this.getInput(consoleManager);
            collectionManager.add((Organization) inputData, id);
            consoleManager.print("Добавлена новая запись.");

        } else {
            consoleManager.print("Элемент с таким id уже существует");
        }
    }
}
