package com.seanhannon.controller;

import com.seanhannon.controller.mapper.DriverMapper;
import com.seanhannon.datatransferobject.DriverDTO;
import com.seanhannon.domainobject.DriverDO;
import com.seanhannon.exception.CarAlreadyInUseException;
import com.seanhannon.exception.ConstraintsViolationException;
import com.seanhannon.exception.DriverAlreadySelectedACarException;
import com.seanhannon.exception.DriverNoCarSelectedException;
import com.seanhannon.exception.EntityNotFoundException;
import com.seanhannon.exception.IllegalSearchException;
import com.seanhannon.exception.IncorrectStatusException;
import com.seanhannon.service.driver.DriverService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController {

    private final DriverService driverService;


    @Autowired
    public DriverController(final DriverService driverService) {
        this.driverService = driverService;
    }


    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable long driverId) throws EntityNotFoundException {
        driverService.delete(driverId);
    }


    @PutMapping("/{driverId}")
    public void updateLocation(
      @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
      throws EntityNotFoundException {
        driverService.updateLocation(driverId, longitude, latitude);
    }

// Commenting out due to findAllByQuery having the same functionality just by query language.
//    @GetMapping
//    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus) {
//        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
//    }

    @PostMapping("/{driverId}/car/{carId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO selectCar(@Valid @PathVariable long driverId, @PathVariable long carId) throws
      IncorrectStatusException, CarAlreadyInUseException, DriverAlreadySelectedACarException, EntityNotFoundException {
        return DriverMapper.makeDriverDTO(driverService.selectCar(driverId, carId));
    }

    @PutMapping("/{driverId}/car")
    public DriverDTO deselectCar(@Valid @PathVariable long driverId) throws DriverNoCarSelectedException, EntityNotFoundException {
        return DriverMapper.makeDriverDTO(driverService.deselectCar(driverId));
    }

    @GetMapping
    public List<DriverDTO> findAllByQuery(@RequestParam(value = "search") String search) throws IllegalSearchException {
        return DriverMapper.makeDriverDTOList(driverService.search(search));
    }
}