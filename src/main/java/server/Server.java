package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import server.enums.ContentType;
import server.enums.ResponseCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class Server {
    private final HttpServer basicServer;
    private static final String dataDirectory = "data";
    private final Map<String, RouteHandler> routes = new HashMap<>();

    protected Server(String host, int port) throws IOException {
        basicServer = createServer(host, port);
        registerCommonHandler();
    }

    private static HttpServer createServer(String host, int port) throws IOException {
        System.out.printf("Starting basicServer on http://%s:%s/%n", host, port);
        InetSocketAddress address = new InetSocketAddress(host, port);
        return HttpServer.create(address, 50);
    }

    public Map<String, RouteHandler> getRoutes() {
        return routes;
    }

    private void registerCommonHandler() {
        basicServer.createContext("/", this::handleIncomingServerRequests);
        registerGet("/", exchange -> sendFile(exchange, makeFilePath("index.html"), ContentType.TEXT_HTML));
        registerFileHandler(".css", ContentType.TEXT_CSS);
        registerFileHandler(".html", ContentType.TEXT_HTML);
        registerFileHandler(".jpg", ContentType.IMAGE_JPEG);
        registerFileHandler(".png", ContentType.IMAGE_PNG);
    }

    protected final void registerGet(String route, RouteHandler handler) {
        getRoutes().put("GET " + route, handler);
    }

    protected final void registerFileHandler(String fileExt, ContentType type) {
        registerGet(fileExt, exchange -> sendFile(exchange, makeFilePath(exchange), type));
    }

    protected final void sendFile(HttpExchange exchange, Path pathToFile, ContentType contentType) {
        try {
            if (Files.notExists(pathToFile)) {
                respond404(exchange);
                return;
            }
            byte[] data = Files.readAllBytes(pathToFile);
            sendByteData(exchange, ResponseCodes.OK, contentType, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path makeFilePath(HttpExchange exchange) {
        return makeFilePath(exchange.getRequestURI().getPath());
    }

    protected Path makeFilePath(String... s) {
        return Path.of(dataDirectory, s);
    }

    private void handleIncomingServerRequests(HttpExchange exchange) {
        RouteHandler route = getRoutes().getOrDefault(makeKey(exchange), this::respond404);
        route.handle(exchange);
    }

    private static String makeKey(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        int index = path.lastIndexOf(".");
        String extOrPath = index == -1 ? path : path.substring(index).toLowerCase();
        return makeKey(method, extOrPath);
    }

    private static String makeKey(String method, String route) {
        return String.format("%s %s", method.toUpperCase(), route);
    }

    private void respond404(HttpExchange exchange) {
        try {
            byte[] bytes = "404 NOT FOUND".getBytes(StandardCharsets.UTF_8);
            sendByteData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected final void sendByteData(HttpExchange exchange,
                                      ResponseCodes responseCode,
                                      ContentType contentType,
                                      byte[] data) throws IOException{
        try (OutputStream outputStream = exchange.getResponseBody()){
            setContentType(exchange, contentType);
            exchange.sendResponseHeaders(responseCode.getCode(), data.length);
            outputStream.write(data);
            outputStream.flush();
        }
    }

    private static void setContentType(HttpExchange exchange, ContentType contentType) {
        exchange.getResponseHeaders().set("Content-Type", String.valueOf(contentType));
    }
}
