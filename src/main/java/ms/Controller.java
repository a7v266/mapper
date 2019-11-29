package ms;

import ms.model.Book;
import ms.model.Car;
import ms.model.Writer;
import ms.service.BookService;
import ms.service.CarService;
import ms.service.GeneratorService;
import ms.service.WriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private BookService bookService;

    @Autowired
    private WriterService writerService;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private CarService carService;

    @GetMapping(path = "/books")
    public Iterable<Book> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping(path = "/writers")
    public Iterable<Writer> getWriters() {
        return writerService.getWriters();
    }

    @PostMapping(path = "/generator")
    public void generateData() {
        generatorService.generateData();
    }

    @PostMapping(path = "/car")
    public Car createCar(@RequestBody Car car) {
        return carService.mergeCar(car);
    }
}

