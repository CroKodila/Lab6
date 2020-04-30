package Сommands;

import Exceptions.InvalidValueException;
import Managers.CollectionManager;
import Managers.ConsoleManager;
import object.Organization;

public class UpdateIDCommand extends Commands {
    public UpdateIDCommand(){
        cmdName = "update";
        argCount = 1;
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
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

        Organization organization = consoleManager.getOrganization();
        collectionManager.update(organization, id);
        consoleManager.print("Элемент с id - " + id + " был изменен");
    }

}
