package Сommands;

import managers.CollectionManager;
import managers.ConsoleManager;

public class PrintAnnualTurnoverCommand extends Commands {
    public PrintAnnualTurnoverCommand(){
        cmdName = "print_field_ascending_annual_turnover";
    }
    @Override
    public void execute(ConsoleManager consoleManager, CollectionManager collectionManager) {
        collectionManager.sortByAnnualTurnover().forEach(x->consoleManager.print(x.toString()));
    }
}
