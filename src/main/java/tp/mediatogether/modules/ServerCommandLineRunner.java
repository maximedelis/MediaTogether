package tp.mediatogether.modules;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer socketIOServer;

    public ServerCommandLineRunner(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @Override
    public void run(String... args) {
        socketIOServer.start();
    }

}
