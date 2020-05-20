package network.packs;

import java.io.Serializable;

public class CommandExecPack implements Serializable {
    private final String message;
    public CommandExecPack(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

