package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class GroupCountingByIDCommand extends Commands {
    public GroupCountingByIDCommand(){
        cmdName = "group_counting_by_id";
    }
    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        consoleManager.print("Четные id " + String.valueOf(collectionManager.getCsvCollection().stream().filter(x->x.getId()%2==0).count()));
        consoleManager.print("Нечетные id " + String.valueOf(collectionManager.getCsvCollection().stream().filter(x->x.getId()%2!=0).count()));
        return null;
    }
}
