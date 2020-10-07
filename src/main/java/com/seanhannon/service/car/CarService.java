package com.seanhannon.service.car;

import com.seanhannon.domainobject.CarDO;
import com.seanhannon.exception.ConstraintsViolationException;
import com.seanhannon.exception.EntityNotFoundException;

import java.util.List;

public interface CarService {

    CarDO create(CarDO carDO) throws EntityNotFoundException, ConstraintsViolationException;

    CarDO find(long carId) throws EntityNotFoundException;

    List<CarDO> findAll();

    void delete(long carId);

    CarDO update(long carId, CarDO carDO) throws EntityNotFoundException;
}
