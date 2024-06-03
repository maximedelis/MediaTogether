package tp.mediatogether.commands;

import com.corundumstudio.socketio.SocketIOClient;
import tp.mediatogether.services.SocketService;

public class SyncCommand implements Command {

    private final SocketService service;

    public SyncCommand(SocketService service) {
        this.service = service;
    }

    @Override
    public void execute(String room, String value, SocketIOClient senderClient) {
        service.sendCommand(room, senderClient, CommandType.SYNC, value);
    }

}
