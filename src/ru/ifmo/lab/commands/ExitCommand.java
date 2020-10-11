package ru.ifmo.lab.commands;

import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.DatabaseController;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.ConsoleManager;

public class ExitCommand extends AbstractCommand {

    public ExitCommand(){
        cmdName = "exit";
        description = "выход из программы без сохранения";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        /*if(consoleManager.getIsScript()) System.exit(1);

        String out = consoleManager.readWithMessage("Вы уверены? Данные не сохраняются (Y/N)", false);
        if(out.toLowerCase().equals("y")) System.exit(1);*/
        System.exit(1);

        return true;
    }
}
