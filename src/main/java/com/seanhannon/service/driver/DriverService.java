package com.seanhannon.service.driver;

import com.seanhannon.domainobject.DriverDO;
import com.seanhannon.domainvalue.OnlineStatus;
import com.seanhannon.exception.CarAlreadyInUseException;
import com.seanhannon.exception.ConstraintsViolationException;
import com.seanhannon.exception.DriverAlreadySelectedACarException;
import com.seanhannon.exception.DriverNoCarSelectedException;
import com.seanhannon.exception.EntityNotFoundException;
import com.seanhannon.exception.IllegalSearchException;
import com.seanhannon.exception.IncorrectStatusException;

import java.util.Collection;
import java.util.List;

public interface DriverService
{

    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;

    List<DriverDO> find(OnlineStatus onlineStatus);

    DriverDO selectCar(long driverId, long carId) throws EntityNotFoundException, CarAlreadyInUseException,
            DriverAlreadySelectedACarException, IncorrectStatusException;

    DriverDO deselectCar(long driverId) throws EntityNotFoundException, DriverNoCarSelectedException;

  Collection<DriverDO> search(String search) throws IllegalSearchException;
}
