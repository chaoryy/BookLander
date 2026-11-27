package server;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import com.sun.net.httpserver.HttpExchange;
import models.Book;
import models.Employee;
import models.enums.Role;
import server.enums.ContentType;
import server.enums.ResponseCodes;
import service.BookLoanService;
import service.BookService;
import service.EmployeeService;
import utils.UrlParseUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookLenderServer extends Server {
    private final BookService bookService;
    private final EmployeeService employeeService;
    private final Configuration freemarker = initFreeMarker();
    private static final int EMPLOYEE_ID = 3;

    public BookLenderServer(String host, int port,
                            BookService bookService,
                            EmployeeService employeeService,
                            BookLoanService bookLoanService) throws IOException {
        super(host, port);
        this.bookService = bookService;
        this.employeeService = employeeService;
        registerRoutes();
    }

    private void registerRoutes() {
        registerGet("/books", this::booksHandler);
        registerGet("/employee", this::employeeHandler);
        registerGet("/book", this::bookHandler);
        registerGet("/employee/current", this::currentBooksHandler);
        registerGet("/employee/prev", this::prevBooksHandler);
        registerGet("/register", this::registerGetHandler);
        registerPost("/register", this::registerPostHandler);
    }

    private void registerGetHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        data.put("roles", Role.values());
        renderTemplate(exchange, ResponseCodes.OK,"register.ftlh", data);
    }

    private void registerPostHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();

        String raw = getBody(exchange);
        Map<String, String> parsed = UrlParseUtils.parseUrlEncoded(raw, "&");
        try {
            String name = validateString(parsed.get("name"));
            String lastName = validateString(parsed.get("lastName"));
            String email = validateString(parsed.get("email"));
            String password = validateString(parsed.get("password"));
            Role role = Role.getRoleByName(parsed.get("role"));
            employeeService.registerNewEmployee(name, lastName, email, password, role);
            redirect(exchange, "/employee");
        } catch (IllegalArgumentException e) {
            data.put("error", e.getMessage());
            data.put("roles", Role.values());
            renderTemplate(exchange, ResponseCodes.BAD_REQUEST,"register.ftlh", data);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String validateString(String str) {
        if (str == null || str.isBlank()) {
            return "";
        }
        return str.trim();
    }

    private void booksHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        data.put("books", bookService.getAllBooks());
        renderTemplate(exchange, ResponseCodes.OK,"books.ftlh", data);
    }

    private void bookHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        int bookId = 101;
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            respond404(exchange);
            return;
        }
        data.put("book", book);
        renderTemplate(exchange, ResponseCodes.OK,"book.ftlh", data);
    }

    private void employeeHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        Employee employee = checkAndGetEmployeeById(exchange, EMPLOYEE_ID);
        if (employee == null) {
            return;
        }
        data.put("employee", employee);
        renderTemplate(exchange, ResponseCodes.OK,"employee.ftlh", data);
    }

    private void employeeBookHandler(HttpExchange exchange,
                                     int employeeId,
                                     Function<Employee, List<Integer>> idsExtractor,
                                     String ftlhName) {
        Map<String, Object> data = new HashMap<>();
        Employee employee = checkAndGetEmployeeById(exchange, employeeId);
        if (employee == null) {
            return;
        }
        data.put("employee", employee);
        data.put("books", checkAndGetBooksById(idsExtractor.apply(employee)));
        renderTemplate(exchange, ResponseCodes.OK, ftlhName, data);
    }

    private void currentBooksHandler(HttpExchange exchange) {
        employeeBookHandler(exchange, EMPLOYEE_ID, Employee::getCurrentBooksId, "current_books.ftlh");
    }

    private void prevBooksHandler(HttpExchange exchange) {
        employeeBookHandler(exchange, EMPLOYEE_ID, Employee::getPrevBooksId, "prev_books.ftlh");
    }

    private Employee checkAndGetEmployeeById(HttpExchange exchange,
                                             int employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            respond404(exchange);
            return null;
        }
        return employee;
    }

    private List<Book> checkAndGetBooksById(List<Integer> booksIds) {
        if (booksIds == null || booksIds.isEmpty()) {
            return List.of();
        }
        return booksIds.stream()
                .map(id -> {
                    Book book = bookService.getBookById(id);
                    if (book == null) {
                        System.out.println("Book with id " + id + " not found.");
                    }
                    return book;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    protected String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        Charset utf8 = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(inputStream, utf8);

        try (BufferedReader reader = new BufferedReader(isr)) {
            return reader.lines().collect(Collectors.joining(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            // путь к каталогу в котором у нас хранятся шаблоны
            // это может быть совершенно другой путь, чем тот, откуда сервер берёт файлы
            // которые отправляет пользователю
            cfg.setDirectoryForTemplateLoading(Path.of("templates").toFile());

            // прочие стандартные настройки о них читать тут
            // https://freemarker.apache.org/docs/pgui_quickstart_createconfiguration.html
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void renderTemplate(HttpExchange exchange, ResponseCodes code, String templateFile, Object dataModel) {
        try {
            // Загружаем шаблон из файла по имени.
            // Шаблон должен находится по пути, указанном в конфигурации
            Template temp = freemarker.getTemplate(templateFile);

            // freemarker записывает преобразованный шаблон в объект класса writer
            // а наш сервер отправляет клиенту массивы байт
            // по этому нам надо сделать "мост" между этими двумя системами

            // создаём поток, который сохраняет всё, что в него будет записано в байтовый массив
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // создаём объект, который умеет писать в поток и который подходит для freemarker
            try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {

                // обрабатываем шаблон заполняя его данными из модели
                // и записываем результат в объект "записи"
                temp.process(dataModel, writer);
                writer.flush();

                // получаем байтовый поток
                var data = stream.toByteArray();

                // отправляем результат клиенту
                sendByteData(exchange, code, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            sendError500(exchange);
        }
    }


}
