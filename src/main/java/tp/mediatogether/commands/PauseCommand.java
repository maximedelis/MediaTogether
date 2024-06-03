package tp.mediatogether.commands;

import com.corundumstudio.socketio.SocketIOClient;
import tp.mediatogether.services.SocketService;

public class PauseCommand implements Command {

    private final SocketService service;

    public PauseCommand(SocketService service) {
        this.service = service;
    }

    @Override
    public void execute(String room, String value, SocketIOClient senderClient) {
        service.sendCommand(room, senderClient, CommandType.PAUSE, "");
    }
}
