package network.packs;

import java.io.Serializable;

public class CommandPack implements Serializable {
    private String cmdName;
    private String[] args;

    public CommandPack(String cmdName, String[] args){
        this.cmdName = cmdName;
        this.args = args;
    }

    public String getCmdName() {
        return cmdName;
    }

    public String[] getArgs() {
        return args;
    }
}
