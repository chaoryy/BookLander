package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import models.Book;
import models.BookLoan;
import models.Employee;

import java.io.IOException;
import java.io.Reader;
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

    public FileUtil() {
    }

    private static <T> List<T> readList(Path path, Type type) {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<T> list = gson.fromJson(reader, type);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Book> readBooksJson() {
        Path path = Path.of("data", "books.json");
        return readList(path, new TypeToken<List<Book>>() {}.getType());
    }

    public static List<Employee> readEmployeesJson() {
        Path path = Path.of("data", "employees.json");
        return readList(path, new TypeToken<List<Employee>>() {}.getType());
    }

    public static List<BookLoan> readLoansJson() {
        Path path = Path.of("data", "book-loans.json");
        return readList(path, new TypeToken<List<BookLoan>>() {}.getType());
    }
}
