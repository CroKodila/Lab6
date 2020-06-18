package Сommands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

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
