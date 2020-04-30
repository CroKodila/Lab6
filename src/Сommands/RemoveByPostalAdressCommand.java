package Сommands;

import Exceptions.InvalidValueException;
import Managers.CollectionManager;
import Managers.ConsoleManager;
import object.Address;

public class RemoveByPostalAdressCommand extends Commands {
    public RemoveByPostalAdressCommand(){
        cmdName = "remove_any_by_postal_address";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        Address address = consoleManager.getpostalAddress();
        collectionManager.remove_by_postal_address(address);
        consoleManager.print("Элемент(ы) удален(ы)");
    }
}
