package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class ClearCommad extends AbstractCommand {
    public ClearCommad(){
        cmdName = "clear";
        description = "очищает коллекцию";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        String retDelAll = databaseController.clearCity(credentials);
        if (retDelAll == null) {
            collectionManager.clear();
            consoleManager.writeln("All elements deleted");
            return true;
        }else{
            consoleManager.writeln("Problem: " + retDelAll);
            return false;
        }
    }
}
