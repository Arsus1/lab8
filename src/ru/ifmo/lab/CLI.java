package ru.ifmo.lab;

import ru.ifmo.lab.exceptions.InvalidValueException;
import ru.ifmo.lab.exceptions.NoCommandException;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.lab.managers.CollectionManager;
import ru.ifmo.lab.managers.CommandsManager;
import ru.ifmo.lab.managers.ConsoleManager;
import ru.ifmo.lab.utils.AppConstant;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@Slf4j
public class CLI {

    public CLI() throws IOException {
        ConsoleManager consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(System.out), false);
        consoleManager.writeln("CLI запущен версия " + AppConstant.VERSION);
        consoleManager.writeln("Используйте help для получения справки");

        log.info("app started");
        log.info("version {}", AppConstant.VERSION);

        CollectionManager collectionManager = new CollectionManager(AppConstant.FILE_PATH);

        while (true) {
            consoleManager.write("> ");
            if (consoleManager.hasNextLine()) {
                String cmd = consoleManager.read().trim();
                if(cmd.equals("")) continue;

                try {
                    CommandsManager.getInstance().execute(cmd, consoleManager, collectionManager);
                }catch (NoCommandException ex) {
                    consoleManager.writeln("Такая команда не найдена :(\nВведите команду help, чтобы вывести спискок команд");
                    log.error(ex.getMessage());
                }catch (NumberFormatException|ClassCastException ex){
                    consoleManager.writeln("Ошибка во время каста\n"+ex.getMessage());
                    log.error(ex.getMessage());
                } catch (InvalidValueException ex){
                    consoleManager.writeln(ex.getMessage());
                    log.error(ex.getMessage());
                }
            }
        }
    }

}
