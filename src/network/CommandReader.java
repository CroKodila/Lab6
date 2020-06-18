package network;



import database.Credentials;
import database.UserDBManager;

import exceptions.AuthentificationException;
import exceptions.InvalidValueException;
import exceptions.NoCommandException;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import managers.CommandManager;

import managers.ConsoleManager;


import network.packs.CommandPack;
import Сommands.*;

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

    public CommandReader(ClientUdpChannel socket, CommandManager commandsManager, ConsoleManager consoleManager) {
        this.channel = socket;
        this.commandsManager = commandsManager;
        this.consoleManager = consoleManager;
    }

    public void startInteraction(Credentials credentials) throws IOException, ArrayIndexOutOfBoundsException, NoCommandException {
        String commandStr;
        consoleManager.print("> ");
        if(consoleManager.hasNextLine()) {
            executeCount = 0;
            executeFault = false;
            sendCommand(consoleManager.read().trim(), consoleManager, credentials);
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
