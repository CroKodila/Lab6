package ru.ifmo.lab;

import ru.ifmo.lab.exceptions.InvalidValueException;
import ru.ifmo.lab.exceptions.NoCommandException;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.CommandManager;
import ru.ifmo.lab.managers.ConsoleManager;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main {
    public static void main(String[] args){

    /* String str = args[0];
        CollectionManager collectionManager = new CollectionManager(str);*/
        /**
         * Главный класс приложения
        */
      CollectionManager collectionManager = new CollectionManager("Organizations.csv");
        ConsoleManager consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(System.out),false);
        consoleManager.print("Используйте help для получения справки");



        while (true) {
            if (consoleManager.hasNextLine()) {
                String cmd = consoleManager.read();

                try {
                    CommandManager.getInstance().execute(cmd, consoleManager, collectionManager);
                }catch (NoCommandException ex) {
                    consoleManager.print("Такой команды не существует \nВведите команду help, чтобы вывести спискок команд");
                }catch (NumberFormatException|ClassCastException ex){
                    consoleManager.print("Ошибка \n Попробуйте заново:("+ex.getMessage()) ;
                } catch (InvalidValueException ex){
                    consoleManager.print(ex.getMessage());
                }
            }
        }
    }

}