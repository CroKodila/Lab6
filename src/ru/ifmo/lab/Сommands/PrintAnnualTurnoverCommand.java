package ru.ifmo.lab.Сommands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class PrintAnnualTurnoverCommand extends Commands {
    public PrintAnnualTurnoverCommand(){
        cmdName = "print_field_ascending_annual_turnover";
    }
    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.sortByAnnualTurnover().forEach(x->consoleManager.print(x.toString()));
        return null;

    }
}
