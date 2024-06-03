package tp.mediatogether.commands;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;
import tp.mediatogether.services.SocketService;

@Service(CommandType.OPEN_BEAN_NAME)
public class StopCommand implements Command {

    private final SocketService service;

    public StopCommand(SocketService service) {
        this.service = service;
    }

    @Override
    public void execute(String room, String value, SocketIOClient senderClient) {
        service.sendCommand(room, senderClient, CommandType.STOP, "");
    }

}
