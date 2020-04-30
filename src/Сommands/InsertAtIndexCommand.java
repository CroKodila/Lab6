package Сommands;

import Exceptions.InvalidValueException;
import Managers.CollectionManager;
import Managers.ConsoleManager;
import object.Organization;

public class InsertAtIndexCommand extends Commands {
    public InsertAtIndexCommand() {
        cmdName = "insert_at";
        argCount = 1;
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
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
            Organization organization = consoleManager.getOrganization();
            collectionManager.add(organization, id);
            consoleManager.print("Добавлена новая запись.");

        } else {
            consoleManager.print("Элемент с таким id уже существует");
        }
    }
}
