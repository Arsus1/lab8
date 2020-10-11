package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class AddCommand extends AbstractCommand {

    public AddCommand(){
        cmdName = "add";
        description = "добавляет новый элемент в коллекцию";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCity();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        String cityID = databaseController.addCity((City) inputData, credentials);
        if (isNumeric(cityID)) {
            ((City) inputData).setId(Long.valueOf(cityID));
            ((City) inputData).setUserID(credentials.id);
            ((City) inputData).setUsername(credentials.username);
            collectionManager.addElement((City) inputData);
            consoleManager.writeln("New city added");
        }

        inputData = null;

        return true;
    }
}
