package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import models.Book;
import models.BookLoan;
import models.Employee;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
        @Override
        public void write(JsonWriter out, LocalDate date) throws IOException {
            if (date == null) {
                out.nullValue();
            } else {
                out.value(date.toString());
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String value = in.nextString();
            return (value == null || value.isBlank()) ? null : LocalDate.parse(value);
        }
    }).create();
    private static final Path BOOKS_PATH = Path.of("data", "books.json");
    private static final Path EMPLOYEES_PATH = Path.of("data", "employees.json");
    private static final Path LOANS_PATH = Path.of("data", "book-loans.json");

    private FileUtil() {
    }

    private static <T> List<T> readList(Path path, Type type) throws IOException {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<T> list = gson.fromJson(reader, type);
            return list == null ? new ArrayList<>() : list;
        } catch (JsonSyntaxException e) {
            throw new IOException("Invalid JSON format in file: " + path, e);
        }
    }

    public static List<Book> readBooksJson() throws IOException {
        return readList(BOOKS_PATH, new TypeToken<List<Book>>() {}.getType());
    }

    public static List<Employee> readEmployeesJson() throws IOException {
        return readList(EMPLOYEES_PATH, new TypeToken<List<Employee>>() {}.getType());
    }

    public static List<BookLoan> readLoansJson() throws IOException {
        return readList(LOANS_PATH, new TypeToken<List<BookLoan>>() {}.getType());
    }


    private static <T> void writeToJson(Path path, List<T> list) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            gson.toJson(list, writer);
        }
    }

    public static void writeBooks(List<Book> books) throws IOException {
        try {
            writeToJson(BOOKS_PATH, books);
        } catch (IOException e) {
            throw new IOException("Could not add new book to" + BOOKS_PATH, e);
        }
    }

    public static void writeEmployees(List<Employee> employees) throws IOException{
        try {
            writeToJson(EMPLOYEES_PATH, employees);
        } catch (IOException e) {
            throw new IOException("Could not add new employee to " + EMPLOYEES_PATH, e);
        }
    }

    public static void writeLoans(List<BookLoan> loans) throws IOException {
        try {
            writeToJson(LOANS_PATH, loans);
        } catch (IOException e) {
            throw new IOException("Could not add new book loans to " + LOANS_PATH, e);
        }
    }

}
