package ru.ifmo.lab.network;



import ru.ifmo.lab.database.Credentials;
import ru.ifmo.lab.database.UserDBManager;

import ru.ifmo.lab.exceptions.AuthentificationException;
import ru.ifmo.lab.exceptions.InvalidValueException;
import ru.ifmo.lab.exceptions.NoCommandException;

import lombok.extern.slf4j.Slf4j;
import ru.ifmo.lab.exceptions.RecursiveExeption;
import ru.ifmo.lab.managers.CommandManager;

import ru.ifmo.lab.managers.ConsoleManager;


import ru.ifmo.lab.network.packs.CommandPack;
import ru.ifmo.lab.ui.listener.EventListener;
import ru.ifmo.lab.Сommands.*;

import javax.security.sasl.AuthenticationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class CommandReader {
    private final ClientUdpChannel channel;
    private final CommandManager commandsManager;
    private final ConsoleManager consoleManager;
    private boolean executeFault = false;
    private int executeCount = 0;
    public EventListener executeEvent;
    private static CommandReader instance;

    public CommandReader(ClientUdpChannel socket, CommandManager commandsManager, ConsoleManager consoleManager) {
        this.channel = socket;
        this.commandsManager = commandsManager;
        this.consoleManager = consoleManager;
        instance = this;
    }

    public static CommandReader getInstance() {
        return instance;
    }

    public void startInteraction(Credentials credentials) throws ArrayIndexOutOfBoundsException, NoCommandException {
        consoleManager.print("> ");
        if(consoleManager.hasNextLine()) {
            executeCount = 0;
            executeFault = false;
            sendCommand(consoleManager.read().trim(), consoleManager, credentials);
        }
    }

    public void sendCommand(Commands cmd, ConsoleManager cMgr, Credentials credentials){
        executeCount = 0;
        executeFault = false;
        if(cmd == null) return;

        try {
            if(cmd instanceof ExitCommand){ finishClient(); }
            else if(cmd instanceof HelpCommand) {
                cmd.execute(cMgr, null, null, credentials);
            }
            else if(cmd instanceof ExecuteScriptCommand){

                if(credentials.username.equals(UserDBManager.DEFAULT_USERNAME)) throw new AuthentificationException("Пожалуйста, авторизуйтесь");
                executeCount++;
                if(executeCount == 127) throw new StackOverflowError();
                Path pathToScript = Paths.get(cmd.getArgs()[0]);
                int lineNum = 1;
                try {
                    cMgr = new ConsoleManager(new FileReader(pathToScript.toFile()), new OutputStreamWriter(System.out), true);
                    for (lineNum=1; cMgr.hasNextLine(); lineNum++) {
                        String line = cMgr.read().trim();
                        if(!line.isEmpty() && !executeFault) { sendCommand(line, cMgr, credentials); }
                    }
                } catch (FileNotFoundException ex) {
                    executeFault = true;
                    consoleManager.print("Файл не найден.");
                    log.error(ex.getMessage());
                }catch (RecursiveExeption ex){
                    consoleManager.print(ex.getMessage());
                    log.error(ex.getMessage());
                }catch (StackOverflowError ex){
                    if(!executeFault) {
                        consoleManager.print("Стек переполнен, выполнение прервано");
                    }

                    executeFault = true;
                }catch (Exception ex){
                    executeFault = true;
                    consoleManager.print(ex.getMessage());
                    log.error(ex.getMessage());
                }

            }
            else {
                if(credentials.username.equals(UserDBManager.DEFAULT_USERNAME)
                        && !(cmd instanceof LoginCommand)
                        && !(cmd instanceof RegisterCommand))
                    throw new AuthentificationException("Пожалуйста, авторизуйтесь");


                //if (cmd.getNeedInput()) cmd.setInputData(cmd.getInput(cMgr));
                channel.sendCommand(new CommandPack(cmd, credentials));
            }

        }catch (NumberFormatException|ClassCastException ex){
            cMgr.print("Ошибка во время каста\n" + ex.getMessage());
            log.error(ex.getMessage());
        } catch (InvalidValueException | AuthentificationException ex){
            cMgr.print(ex.getMessage());
            log.error(ex.getMessage());
        }

    }




    public void sendCommand(String sCmd, ConsoleManager cMgr, Credentials credentials){
        if(sCmd.isEmpty() || executeFault) return;
        try {
            Commands cmd = commandsManager.parseCommand(sCmd);
            if(cmd instanceof ExitCommand){ finishClient(); }
            else if(cmd instanceof HelpCommand) {
                cmd.execute(cMgr, null, null, credentials);
            }
            else if(cmd instanceof ExecuteScriptCommand){

                if(credentials.username.equals(UserDBManager.DEFAULT_USERNAME)) throw new AuthentificationException("Пожалуйста, авторизуйтесь");
                executeCount++;
                if(executeCount == 127) throw new StackOverflowError();
                Path pathToScript = Paths.get(cmd.getArgs()[0]);
                int lineNum = 1;
                try {
                    cMgr = new ConsoleManager(new FileReader(pathToScript.toFile()), new OutputStreamWriter(System.out), true);
                    for (lineNum=1; cMgr.hasNextLine(); lineNum++) {
                        String line = cMgr.read().trim();
                        if(!line.isEmpty() && !executeFault) { sendCommand(line, cMgr, credentials); }
                    }

                } catch (FileNotFoundException ex) {
                    executeFault = true;
                    consoleManager.print("Файла не найден.");
                    log.error(ex.getMessage());
                    executeEvent.onError("File not found");
                }catch (RecursiveExeption ex){
                    consoleManager.print(ex.getMessage());
                    log.error(ex.getMessage());
                }catch (StackOverflowError ex){
                    if(!executeFault) {
                        consoleManager.print("Стек переполнен, выполнение прервано");
                        executeEvent.onError("StackOverflowError");
                    }

                    executeFault = true;
                }catch (Exception ex){
                    executeFault = true;
                    consoleManager.print(ex.getMessage());
                    log.error(ex.getMessage());
                }

            }
            else {
                if(credentials.username.equals(UserDBManager.DEFAULT_USERNAME)
                        && !(cmd instanceof LoginCommand)
                        && !(cmd instanceof RegisterCommand))
                    throw new AuthentificationException("Пожалуйста, авторизуйтесь");


                if (cmd.getNeedInput()) cmd.setInputData(cmd.getInput(cMgr));
                channel.sendCommand(new CommandPack(cmd, credentials));
            }
        }catch (NoCommandException ex) {
            cMgr.print("Такая команда не найдена :(\nВведите команду help, чтобы вывести спискок команд");
            log.error(ex.getMessage());
        }catch (NumberFormatException|ClassCastException ex){
            cMgr.print("Ошибка во время каста\n" + ex.getMessage());
            log.error(ex.getMessage());
        } catch (InvalidValueException | AuthentificationException ex){
            cMgr.print(ex.getMessage());
            log.error(ex.getMessage());
        }
    }

    public void finishClient() {
        log.info("Finishing client");
        channel.disconnect();
        System.exit(0);
    }
}