package com.seanhannon.controller.mapper;

import com.seanhannon.datatransferobject.CarDTO;
import com.seanhannon.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper {

  public static CarDTO makeCarDTO(CarDO car) {
    CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder().setId(car.getId())
      .setLicensePlate(car.getLicensePlate()).setSeatCount(car.getSeatCount())
      .setConvertible(car.isConvertible()).setRating(car.getRating()).setEngineType(car.getEngineType())
      .setManufacturer(car.getManufacturer());

    return carDTOBuilder.createCarDTO();
  }

  public static CarDO makeCarDO(CarDTO carDTO) {
    return new CarDO(carDTO.getLicensePlate(), carDTO.getSeatCount(), carDTO.isConvertible(),
      carDTO.getRating(), carDTO.getEngineType(), carDTO.getManufacturer());
  }

  public static List<CarDTO> makeCarDTOList(Collection<CarDO> cars) {
    return cars.stream().map(CarMapper::makeCarDTO).collect(Collectors.toList());
  }
}
