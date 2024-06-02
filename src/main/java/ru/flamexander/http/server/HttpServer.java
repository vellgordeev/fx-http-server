package ru.flamexander.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.flamexander.http.server.application.Storage;
import ru.flamexander.http.server.application.handlers.CookiesHandler;
import ru.flamexander.http.server.application.handlers.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private final int port;
    private Dispatcher dispatcher;
    private ExecutorService executorService;
    private ThreadLocal<byte[]> requestBuffer;
    private Handler handler;

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class.getName());

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        executorService = Executors.newFixedThreadPool(4);
        requestBuffer = new ThreadLocal<>();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту: {}", port);
            this.dispatcher = new Dispatcher();
            this.handler = new CookiesHandler();
            Storage.init();
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> executeRequest(socket));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    private void executeRequest(Socket socket) {
        try {
            if (requestBuffer.get() == null) {
                requestBuffer.set(new byte[DEFAULT_BUFFER_SIZE]);
            }
            byte[] buffer = requestBuffer.get();
            int n = socket.getInputStream().read(buffer);
            if (n > 0) {
                String rawRequest = new String(buffer, 0, n);
                HttpRequest request = new HttpRequest(rawRequest);
                HttpResponse response = new HttpResponse(socket.getOutputStream());

                dispatcher.execute(request, handler.handle(request, response));
                socket.getOutputStream().flush();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
