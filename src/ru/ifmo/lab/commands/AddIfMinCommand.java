package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class AddIfMinCommand extends AbstractCommand {

    public AddIfMinCommand(){
        cmdName = "add_if_min";
        description = "добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCity();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        Boolean res = collectionManager.addIfMin((City) inputData);

        if(res)
            consoleManager.writeln("Элемент был добавлен");
        else
            consoleManager.writeln("Элемент не был добавлен. Элемент оказался больше минимального");

        inputData = null;

        return null;
    }
}
