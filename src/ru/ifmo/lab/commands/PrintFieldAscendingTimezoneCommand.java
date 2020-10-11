package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class PrintFieldAscendingTimezoneCommand extends AbstractCommand {

    public PrintFieldAscendingTimezoneCommand(){
        cmdName = "print_field_ascending_timezone";
        description = "выводит значения поля timezone всех элементов в порядке возрастания";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.sortByTimezone().forEach(x->consoleManager.writeln(x.toString()));

        return true;
    }
}
