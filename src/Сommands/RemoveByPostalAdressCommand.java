package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;
import object.Address;

public class RemoveByPostalAdressCommand extends Commands {
    public RemoveByPostalAdressCommand(){
        cmdName = "remove_any_by_postal_address";
        needInput = true;
    }
    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getpostalAddress();
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        collectionManager.remove_by_postal_address((Address)inputData);
        consoleManager.print("Элемент(ы) удален(ы)");
    }
}
