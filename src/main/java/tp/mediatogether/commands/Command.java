package tp.mediatogether.commands;

import com.corundumstudio.socketio.SocketIOClient;

public interface Command {
    void execute(String room, String value, SocketIOClient senderClient);
}
