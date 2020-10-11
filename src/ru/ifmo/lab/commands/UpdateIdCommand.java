package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.models.City;
import ru.ifmo.lab.exceptions.InvalidValueException;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class UpdateIdCommand extends AbstractCommand {
    public UpdateIdCommand(){
        cmdName = "update";
        description = "обновляет значение элемента коллекции, id которого равен заданному";
        argCount = 1;
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getCity();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            throw new InvalidValueException("Format error");
        }

        String cityID = databaseController.updateCity(id, (City) inputData, credentials);
        if (cityID == null) {
            if(collectionManager.update((City) inputData, (long)id)) {
                consoleManager.writeln("Element with id(" + id + ") - edited");

                inputData = null;
                return true;
            } else {
                consoleManager.writeln("Element with id(" + id + ") - doesn't");

                inputData = null;
                return false;
            }

        } else {
            consoleManager.writeln("Have some problems: " + cityID);

            inputData = null;
            return false;
        }

    }
}
