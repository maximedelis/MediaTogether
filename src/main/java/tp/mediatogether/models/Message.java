package tp.mediatogether.models;

import tp.mediatogether.commands.CommandType;

public class Message {
    private String room;
    private CommandType commandName;
    private String value;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public CommandType getCommandName() {
        return commandName;
    }

    public void setCommandName(CommandType commandName) {
        this.commandName = commandName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
