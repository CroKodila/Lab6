import Exceptions.EmptyFileException;
import Exceptions.InvalidValueException;
import Exceptions.NoCommandException;
import Managers.CollectionManager;
import Managers.CommandManager;
import Managers.ConsoleManager;
import ParserCSV.CSVFile;
import ParserCSV.CSVManager;
import object.Address;
import object.Coordinates;
import object.Organization;
import object.OrganizationType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

     String str = args[0];
        CollectionManager collectionManager = new CollectionManager(str);
        /**
         * Главный класс приложения
        */
      // CollectionManager collectionManager = new CollectionManager("Organizations.csv");
        ConsoleManager consoleManager = new ConsoleManager(new InputStreamReader(System.in),false);
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