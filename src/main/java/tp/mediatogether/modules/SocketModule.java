package tp.mediatogether.modules;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tp.mediatogether.models.Message;
import tp.mediatogether.services.CommandHandlerService;

@Component
public class SocketModule {

    private static final Logger log = LoggerFactory.getLogger(SocketModule.class);
    private final CommandHandlerService commandHandlerService;

    public SocketModule(SocketIOServer server, CommandHandlerService commandHandlerService) {
        this.commandHandlerService = commandHandlerService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("command", Message.class, onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            commandHandlerService.executeCommand(data.getCommandName(),
                    data.getRoom(),
                    senderClient,
                    data.getValue());};
    }


    private ConnectListener onConnected() {
        return (client) -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Socket ID[{}] - room[{}]  Connected to chat module through", client.getSessionId().toString(), room);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
        };
    }

}
