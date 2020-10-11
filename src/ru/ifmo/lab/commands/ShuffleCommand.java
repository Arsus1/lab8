package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class ShuffleCommand extends AbstractCommand {
    public ShuffleCommand(){
        cmdName = "shuffle";
        description = "перемешивает элементы коллекции в случайном порядке";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.shuffle();
        consoleManager.writeln("Коллекция была перемешана в случайном порядке");

        return true;
    }
}
