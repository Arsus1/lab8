package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.models.Human;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class CountLessThanGovernor extends AbstractCommand {
    public CountLessThanGovernor(){
        cmdName = "count_less_than_governor";
        description = "выводит количество элементов, значение поля governor которых меньше заданного";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getHuman();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        long count = collectionManager.countLessThanGovernor((Human) inputData);
        consoleManager.writeln("Count elements less than governor: " + count);

        inputData = null;

        return true;
    }
}
