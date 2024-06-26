package tp.mediatogether.commands;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;
import tp.mediatogether.services.SocketService;

@Service(CommandType.SYNC_BEAN_NAME)
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
