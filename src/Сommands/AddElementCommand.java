package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;
import object.Organization;

public class AddElementCommand extends Commands {
    public AddElementCommand (){
      cmdName = "add";
    };
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        Organization organization = consoleManager.getOrganization();
        collectionManager.add(organization);

        consoleManager.print("Добавлена новая запись.");
    }
}
