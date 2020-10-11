package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

import java.util.ArrayList;

public class FilterContainsNameCommand extends AbstractCommand {
    public FilterContainsNameCommand(){
        cmdName = "filter_contains_name";
        description = "выводит элементы, значение поля name которых содержит заданную подстроку";
        argCount = 1;
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        ArrayList<City> out = collectionManager.findByName(args[0]);
        if(out.size() == 0) consoleManager.writeln("Records doesn't exist");
        out.forEach(x-> consoleManager.writeln(x.toString()));

        return true;
    }
}
