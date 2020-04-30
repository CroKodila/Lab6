package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class SaveCommand extends Commands {
    public SaveCommand(){
        cmdName = "save";
        description = "сохраняет коллекцию в файл";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
      collectionManager.save();
        consoleManager.print("Данные сохранены.");
    }
}
