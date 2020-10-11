package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class SaveCommand extends AbstractCommand {

    public SaveCommand(){
        cmdName = "save";
        description = "сохраняет коллекцию в файл";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        collectionManager.save();
        consoleManager.writeln("Коллекция была сохранена.");

        return true;
    }
}
