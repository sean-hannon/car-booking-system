package com.seanhannon.controller;

import com.seanhannon.controller.mapper.CarMapper;
import com.seanhannon.datatransferobject.CarDTO;
import com.seanhannon.domainobject.CarDO;
import com.seanhannon.exception.ConstraintsViolationException;
import com.seanhannon.exception.EntityNotFoundException;
import com.seanhannon.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/cars")
public class CarController {

    @Autowired
    CarService carService;

    @GetMapping
    public List<CarDTO> getCars(){
        return CarMapper.makeCarDTOList(carService.findAll());
    }

    @GetMapping("/{carId}")
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException{
        return CarMapper.makeCarDTO(carService.find(carId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws EntityNotFoundException,
            ConstraintsViolationException {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDO));
    }

    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable long carId){
        carService.delete(carId);
    }

    @PutMapping("/{carId}")
    public CarDTO update(@PathVariable long carId, @Valid @RequestBody CarDTO carDTO) throws EntityNotFoundException{
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.update(carId, carDO));
    }
}
