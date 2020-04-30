package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class GroupCountingByIDCommand extends Commands {
    public GroupCountingByIDCommand(){
        cmdName = "group_counting_by_id";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        consoleManager.print("Четные id " + String.valueOf(collectionManager.getCsvCollection().stream().filter(x->x.getId()%2==0).count()));
        consoleManager.print("Нечетные id " + String.valueOf(collectionManager.getCsvCollection().stream().filter(x->x.getId()%2!=0).count()));
    }
}
