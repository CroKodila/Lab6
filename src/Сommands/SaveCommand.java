package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class SaveCommand extends Commands {
    public SaveCommand(){
        cmdName = "save";
        description = "сохраняет коллекцию в файл";
    }

    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
      collectionManager.save();
        consoleManager.print("Данные сохранены.");
    }
}
