import repository.BookLoanRepository;
import repository.BookRepository;
import repository.EmployeeRepository;
import server.BookLenderServer;
import service.BookLoanService;
import service.BookService;
import service.EmployeeService;
import utils.FileUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Репозитории
            BookRepository bookRepository = new BookRepository(FileUtil.readBooksJson());
            EmployeeRepository employeeRepository = new EmployeeRepository(FileUtil.readEmployeesJson());
            BookLoanRepository bookLoanRepository = new BookLoanRepository(FileUtil.readLoansJson());

            // 2. Сервисы
            BookService bookService = new BookService(bookRepository);
            EmployeeService employeeService = new EmployeeService(employeeRepository);
            BookLoanService bookLoanService = new BookLoanService(
                    bookLoanRepository,
                    bookService,
                    employeeService
            );
            // 3. Сервер и запуск
            BookLenderServer server = new BookLenderServer(
                    "localhost",
                    9889,
                    bookService,
                    employeeService,
                    bookLoanService
            );
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}