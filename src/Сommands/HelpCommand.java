package Сommands;

import Managers.CollectionManager;
import Managers.ConsoleManager;

public class HelpCommand extends Commands {
    public HelpCommand(){
        cmdName = "help";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager, String[] args) {
            consoleManager.print("help : вывести справку по доступным командам\n" +
                    "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                    "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                    "add {element} : добавить новый элемент в коллекцию\n" +
                    "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                    "remove_by_id id : удалить элемент из коллекции по его id\n" +
                    "clear : очистить коллекцию\n" +
                    "save : сохранить коллекцию в файл\n" +
                    "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                    "exit : завершить программу (без сохранения в файл)\n" +
                    "insert_at index {element} : добавить новый элемент в заданную позицию\n" +
                    "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                    "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                    "remove_any_by_postal_address postalAddress : удалить из коллекции один элемент, значение поля postalAddress которого эквивалентно заданному\n" +
                    "group_counting_by_id : сгруппировать элементы коллекции по значению поля id, вывести количество элементов в каждой группе\n" +
                    "print_field_ascending_annual_turnover : вывести значения поля annualTurnover всех элементов в порядке возрастания");
        }

    }

