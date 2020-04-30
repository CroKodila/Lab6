package Сommands;

import Exceptions.EmptyFileException;
import Exceptions.InvalidValueException;
import Exceptions.NoCommandException;
import Managers.CollectionManager;
import Managers.CommandManager;
import Managers.ConsoleManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecuteScriptCommand extends Commands {
    public ExecuteScriptCommand(){
        cmdName = "execute_script";
        argCount = 1;
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
        if (args.length < argCount) {
            throw new InvalidValueException("Введено " + args.length + " аргументов, ожидалось " + argCount);
        }

        Path pathToScript = Paths.get(args[0]);
        consoleManager.print("Идет выполнение скрипта: " + pathToScript.getFileName());
        int lineNum = 1;
        try {
            ConsoleManager _consoleManager = new ConsoleManager(new FileReader(pathToScript.toFile()), true);
            for (lineNum=1; _consoleManager.hasNextLine(); lineNum++) {
                String line = _consoleManager.read();
                CommandManager.getInstance().execute(line, _consoleManager, collectionManager);
            }

        } catch (FileNotFoundException e) {
            consoleManager.print("Файла скрипта не найден.");
        }catch (Exception ex){
            consoleManager.print("\n\t" + ex.getMessage() + "\n\tError on line " + lineNum);
        }catch (StackOverflowError ex){
            consoleManager.print("Стек переполнен, выполнение прервано");
        } catch (NoCommandException e) {
            e.printStackTrace();
        }

    }
}

