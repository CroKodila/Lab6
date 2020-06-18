package network.packs;

import database.Credentials;
import Сommands.Commands;

import java.io.Serializable;

public class CommandPack implements Serializable {
    private Commands command;
    private Credentials credentials;

    public CommandPack(Commands command, Credentials credentials){
        this.command = command;
        this.credentials = credentials;
    }

    public Commands getCommand() {
        return command;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
