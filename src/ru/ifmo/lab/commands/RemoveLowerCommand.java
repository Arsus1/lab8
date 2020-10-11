package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(){
        cmdName = "remove_lower";
        description = "удаляет из коллекции все элементы, меньшие, чем заданный";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCity();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);

        int initSize = collectionManager.getCityCollection().size();
        collectionManager.removeLower((City) inputData);
        int afterSize = collectionManager.getCityCollection().size();

        consoleManager.writeln("Было удалено " + (initSize - afterSize) + " элементов");

        inputData = null;

        return true;
    }
}
