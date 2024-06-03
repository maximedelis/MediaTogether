package tp.mediatogether.commands;

public class CommandResponse {
    private final CommandType commandName;
    private final String value;

    public CommandResponse(CommandType commandName, String value) {
        this.commandName = commandName;
        this.value = value;
    }

    public CommandType getCommandName() {
        return commandName;
    }

    public String getValue() {
        return value;
    }


}
