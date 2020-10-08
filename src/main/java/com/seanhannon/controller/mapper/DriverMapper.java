package com.seanhannon.controller.mapper;

import com.seanhannon.datatransferobject.DriverDTO;
import com.seanhannon.domainobject.CarDO;
import com.seanhannon.domainobject.DriverDO;
import com.seanhannon.domainvalue.GeoCoordinate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DriverMapper {

  public static DriverDO makeDriverDO(DriverDTO driverDTO) {
    return new DriverDO(driverDTO.getUsername(), driverDTO.getPassword());
  }

  public static DriverDTO makeDriverDTO(DriverDO driverDO) {
    DriverDTO.DriverDTOBuilder driverDTOBuilder = DriverDTO.newBuilder()
      .setId(driverDO.getId())
      .setPassword(driverDO.getPassword())
      .setUsername(driverDO.getUsername())
      .setOnlineStatus(driverDO.getOnlineStatus());

    GeoCoordinate coordinate = driverDO.getCoordinate();
    if (coordinate != null) {
      driverDTOBuilder.setCoordinate(coordinate);
    }

    CarDO carDO = driverDO.getCar();
    if (carDO != null) {
      driverDTOBuilder.setCarId(carDO.getId());
    }

    return driverDTOBuilder.createDriverDTO();
  }


  public static List<DriverDTO> makeDriverDTOList(Collection<DriverDO> drivers) {
    return drivers.stream()
      .map(DriverMapper::makeDriverDTO)
      .collect(Collectors.toList());
  }
}
