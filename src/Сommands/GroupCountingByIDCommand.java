package Сommands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

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
