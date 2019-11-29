package ms.service;

import ms.model.Car;

public interface CarService {

    Car saveCar(Car car);

    Car mergeCar(Car car);
}
