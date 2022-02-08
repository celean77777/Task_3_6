import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MyServer {
    private static final Logger LOGGER = LogManager.getLogger(MyServer.class.getName());
    private final int PORT = 8189;
    private List<ClientHandler> clients;

    public MyServer() {
        LOGGER.info("LOGGER: Сервер запущен!");
        DBservices.dbStart();
        try {
            DBservices.readEx();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ExecutorService execut = Executors.newCachedThreadPool();

        try (ServerSocket server = new ServerSocket(PORT)) {
            LOGGER.info("LOGGER: Сервис аутентификации запущен");
            clients = new ArrayList<>();
            while (true) {
                    LOGGER.info("LOGGER: Сервер ожидает подключения");
                    Socket socket = server.accept();
                    execut.execute(() -> {
                        LOGGER.info("LOGGER: Клиент подключился");
                        long startTime = System.currentTimeMillis()/1000;
                        new ClientHandler(this, socket, startTime);
                    });
            }
        } catch (IOException e) {
            LOGGER.error("LOGGER: Ошибка в работе сервера");
        }
        finally {
            LOGGER.info("LOGGER: Сервис аутентификации остановлен");
            execut.shutdown();
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMsg(String msg) {
        LOGGER.info("LOGGER: Broadcast сообщение: " + msg);
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public synchronized void sendMsgToClient(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nickTo)) {
                o.sendMsg("от " + from.getName() + ": " + msg);
                from.sendMsg("клиенту " + nickTo + ": " + msg);
                LOGGER.info("LOGGER: Сообщение: " + msg + " /от кого: " + from.getName() + " /кому: " + nickTo);
                return;
            }
        }
        from.sendMsg("Участника с ником " + nickTo + " нет в чат-комнате");
    }

    public synchronized void broadcastClientsList() {
        StringBuilder sb = new StringBuilder("/clients ");
        for (ClientHandler o : clients) {
            sb.append(o.getName() + " ");
        }
        broadcastMsg(sb.toString());
    }



    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
        LOGGER.info("LOGGER: Клиент " + o.getName() + " вышел из чата!");
        broadcastClientsList();
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
        LOGGER.info("LOGGER: Клиент " + o.getName() + " вошел в чат!");
        broadcastClientsList();
    }



}
