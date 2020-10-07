package com.seanhannon.service.driver;

import com.seanhannon.dataaccessobject.CarRepository;
import com.seanhannon.dataaccessobject.DriverRepository;
import com.seanhannon.dataaccessobject.rsql.CustomRsqlVisitor;
import com.seanhannon.domainobject.CarDO;
import com.seanhannon.domainobject.DriverDO;
import com.seanhannon.domainvalue.GeoCoordinate;
import com.seanhannon.domainvalue.OnlineStatus;
import com.seanhannon.exception.CarAlreadyInUseException;
import com.seanhannon.exception.ConstraintsViolationException;
import com.seanhannon.exception.DriverAlreadySelectedACarException;
import com.seanhannon.exception.DriverNoCarSelectedException;
import com.seanhannon.exception.EntityNotFoundException;

import java.util.Collection;
import java.util.List;

import com.seanhannon.exception.IllegalSearchException;
import com.seanhannon.exception.IncorrectStatusException;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DefaultDriverService implements DriverService {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

  @Autowired
  private DriverRepository driverRepository;

  @Autowired
  private CarRepository carRepository;

  @Override
  public DriverDO find(Long driverId) throws EntityNotFoundException {
    return findDriverChecked(driverId);
  }

  @Override
  public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException {
    DriverDO driver;
    try {
      driver = driverRepository.save(driverDO);
    } catch (DataIntegrityViolationException e) {
      LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
      throw new ConstraintsViolationException(e.getMessage());
    }
    return driver;
  }


  @Override
  @Transactional
  public void delete(Long driverId) throws EntityNotFoundException {
    DriverDO driverDO = findDriverChecked(driverId);
    driverDO.setDeleted(true);
  }


  @Override
  @Transactional
  public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException {
    DriverDO driverDO = findDriverChecked(driverId);
    driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
  }


  @Override
  public List<DriverDO> find(OnlineStatus onlineStatus) {
    return driverRepository.findByOnlineStatus(onlineStatus);
  }

  @Override
  public DriverDO selectCar(long driverId, long carId) throws EntityNotFoundException, CarAlreadyInUseException,
    DriverAlreadySelectedACarException, IncorrectStatusException {
    CarDO car = findCarChecked(carId);
    DriverDO driver = findDriverChecked(driverId);
    if (driver.getOnlineStatus() == OnlineStatus.OFFLINE) {
      throw new IncorrectStatusException("Driver must be ONLINE to select a car");
    }

    CarDO currentCar = driver.getCar();
    if (currentCar != null) {
      throw new DriverAlreadySelectedACarException(
        "Driver has already selected a car, please deselect to pick a new car");
    }

    if (car.isSelected()) {
      throw new CarAlreadyInUseException("Car has already been selected by another driver");
    }
    car.setSelected(true);
    carRepository.save(car);
    driver.setCar(car);
    return driverRepository.save(driver);
  }

  @Override
  public DriverDO deselectCar(long driverId) throws EntityNotFoundException, DriverNoCarSelectedException {
    DriverDO driverDO = findDriverChecked(driverId);
    if (driverDO.getCar() == null) {
      throw new DriverNoCarSelectedException("Driver has not previously selected a car");
    }
    CarDO car = driverDO.getCar();
    car.setSelected(false);
    carRepository.save(car);
    driverDO.setCar(null);
    return driverRepository.save(driverDO);
  }

  @Override
  public Collection<DriverDO> search(String search) throws IllegalSearchException {
    try {
      Node rootNode = new RSQLParser().parse(search);
      Specification<DriverDO> spec = rootNode.accept(new CustomRsqlVisitor<>());
      return driverRepository.findAll(spec);
    } catch (RSQLParserException exception) {
      throw new IllegalSearchException("Search request is invalid, search request: " + search);
    } catch (InvalidDataAccessApiUsageException exception) {
      throw new IllegalSearchException("Search request is invalid, search request: " + search);
    }
  }


  private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException {
    return driverRepository.findById(driverId)
      .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
  }

  private CarDO findCarChecked(Long carId) throws EntityNotFoundException {
    return carRepository.findById(carId).orElseThrow(() -> {
      LOG.warn("Car does not exist for id:  " + carId);
      return new EntityNotFoundException("Car does not exist for id: " + carId);
    });
  }

}
