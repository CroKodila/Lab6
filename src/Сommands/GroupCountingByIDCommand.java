package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class GroupCountingByIDCommand extends Commands {
    public GroupCountingByIDCommand(){
        cmdName = "group_counting_by_id";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        consoleManager.print("Четные id " + String.valueOf(collectionManager.getCsvCollection().stream().filter(x->x.getId()%2==0).count()));
        consoleManager.print("Нечетные id " + String.valueOf(collectionManager.getCsvCollection().stream().filter(x->x.getId()%2!=0).count()));
    }
}
