package com.seanhannon.service.car;

import com.seanhannon.dataaccessobject.CarRepository;
import com.seanhannon.domainobject.CarDO;
import com.seanhannon.exception.ConstraintsViolationException;
import com.seanhannon.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DefaultCarService implements CarService {

    @Autowired
    CarRepository carRepository;


    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException {
        CarDO car;
        try {
            car = carRepository.save(carDO);
        } catch (DataIntegrityViolationException exception){
            LOG.warn("Unable to create car for {}", carDO, exception);
            throw new ConstraintsViolationException(exception.getMessage());
        }
        return car;
    }

    @Override
    public CarDO find(long carId) throws EntityNotFoundException {
        return carRepository.findById(carId).orElseThrow(() -> new EntityNotFoundException("Unable to find car for id: "
                + carId));
    }

    @Override
    public List<CarDO> findAll() {
        return StreamSupport.stream(carRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public void delete(long carId) {
        carRepository.deleteById(carId);
    }

    @Override
    public CarDO update(long carId, CarDO carDOUpdate) throws EntityNotFoundException {
        CarDO carDO = carRepository.findById(carId).orElseThrow(() -> {
            LOG.warn("Car does not exist for id:  " +  carId);
            return new EntityNotFoundException("Car does not exist for id: " + carId);
        });
        carDO.setLicensePlate(carDOUpdate.getLicensePlate());
        carDO.setSeatCount(carDOUpdate.getSeatCount());
        carDO.setConvertible(carDOUpdate.isConvertible());
        carDO.setRating(carDOUpdate.getRating());
        carDO.setEngineType(carDOUpdate.getEngineType());
        carDO.setManufacturer(carDOUpdate.getManufacturer());
        return carRepository.save(carDO);
    }
}
