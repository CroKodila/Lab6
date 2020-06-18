package network.packs;

import java.io.Serializable;

public class CommandExecPack implements Serializable {
    private final Object message;
    public CommandExecPack(Object message){
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }
}

