package tp.mediatogether.commands;

import tp.mediatogether.services.SocketService;
import com.corundumstudio.socketio.SocketIOClient;

public class PlayCommand implements Command {

    private final SocketService service;

    public PlayCommand(SocketService service) {
        this.service = service;
    }

    @Override
    public void execute(String room, String value,SocketIOClient senderClient) {
        service.sendCommand(room, senderClient, CommandType.PLAY, "");
    }

}
