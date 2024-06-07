package tp.mediatogether.services;

import com.corundumstudio.socketio.SocketIOClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tp.mediatogether.commands.CommandResponse;
import tp.mediatogether.commands.CommandType;

@Service
public class SocketService {

    private static final Logger log = LoggerFactory.getLogger(SocketService.class);

    public void sendCommand(String room, SocketIOClient senderClient, CommandType commandName, String commandValue) {
        log.info("Command sent: {}", commandName.toString());
        for (
                SocketIOClient clients : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!clients.getSessionId().equals(senderClient.getSessionId())) {
                System.out.println(new CommandResponse(commandName, commandValue));
                clients.sendEvent("get_command", new CommandResponse(commandName, commandValue));
            }
        }
    }

}
