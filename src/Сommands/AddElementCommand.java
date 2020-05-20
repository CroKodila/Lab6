package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;
import object.Organization;

public class AddElementCommand extends Commands {
    public AddElementCommand (){
      cmdName = "add";
        needInput = true;
    };
    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getOrganization();
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        collectionManager.add((Organization)inputData);

        consoleManager.print("Добавлена новая запись.");
    }
}
