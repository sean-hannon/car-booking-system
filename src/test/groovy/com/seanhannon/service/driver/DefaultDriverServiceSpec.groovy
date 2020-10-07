package com.seanhannon.service.driver

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.seanhannon.dataaccessobject.CarRepository
import com.seanhannon.dataaccessobject.DriverRepository
import com.seanhannon.domainobject.CarDO
import com.seanhannon.domainobject.DriverDO
import com.seanhannon.domainvalue.EngineType
import com.seanhannon.domainvalue.OnlineStatus
import com.seanhannon.exception.CarAlreadyInUseException
import com.seanhannon.exception.DriverAlreadySelectedACarException
import com.seanhannon.exception.DriverNoCarSelectedException
import com.seanhannon.exception.EntityNotFoundException
import com.seanhannon.exception.IncorrectStatusException
import spock.lang.Specification

class DefaultDriverServiceSpec extends Specification{
  @Subject
  DefaultDriverService driverService

  @Collaborator
  DriverRepository driverRepository = Mock()

  @Collaborator
  CarRepository carRepository = Mock()

  def "Select car for driver"(){
    given:
    CarDO carDo = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC,"Mazda")
    DriverDO driverDO = new DriverDO("Paul", "45678")
    driverDO.setOnlineStatus(OnlineStatus.ONLINE)
    driverDO.setCar(null)

    CarDO carDoAfterSelected = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "Mazda")
    carDoAfterSelected.setSelected(true)
    DriverDO driverDOAfterSelected = new DriverDO("Paul", "45678")
    driverDOAfterSelected.setOnlineStatus(OnlineStatus.ONLINE)
    driverDOAfterSelected.setCar(carDoAfterSelected)

    when:
    DriverDO result = driverService.selectCar(1, 2)

    then:
    1 * driverRepository.findById(_) >> Optional.of(driverDO)
    1 * carRepository.findById(_) >> Optional.of(carDo)
    1 * carRepository.save(_) >> carDoAfterSelected
    1 * driverRepository.save(_) >> driverDOAfterSelected

    assert result
    assert result.car == carDoAfterSelected
    assert result.onlineStatus == OnlineStatus.ONLINE
    assert result.car.selected == true
  }

  def "Throw EntityNotFoundException for unknown driver Id"(){
    given:
    CarDO carDo = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC,"Mazda")

    when:
    driverService.selectCar(1, 2)

    then:
    1 * carRepository.findById(_) >> Optional.of(carDo)
    1 * driverRepository.findById(_) >> Optional.empty()
    EntityNotFoundException exception = thrown()
    assert exception.message == "Could not find entity with id: 1"
  }

  def "Throw IncorrectStatusException for an offline driver"() {
    given:
    DriverDO driverDO = new DriverDO("Dave", "Password1")
    CarDO carDo = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC,"Mazda")

    when:
    driverService.selectCar(1, 2)

    then:
    1 * carRepository.findById(_) >> Optional.of(carDo)
    1 * driverRepository.findById(_) >> Optional.of(driverDO)
    IncorrectStatusException exception = thrown()
    assert exception.message == "Driver must be ONLINE to select a car"
  }

  def "Throw DriverAlreadySelectedACarException for a driver that has already selected a car"(){
    given:
    CarDO carDo = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "Mazda")
    DriverDO driverDO = new DriverDO("Paul", "45678")
    driverDO.setOnlineStatus(OnlineStatus.ONLINE)
    driverDO.setCar(carDo)

    when:
    driverService.selectCar(1, 2)

    then:
    1 * carRepository.findById(_) >> Optional.of(carDo)
    1 * driverRepository.findById(_) >> Optional.of(driverDO)
    DriverAlreadySelectedACarException exception = thrown()
    assert exception.message == "Driver has already selected a car, please deselect to pick a new car"
  }

  def "Throw EntityNotFoundException for unknown carId when trying to select a car"(){
    when:
    driverService.selectCar(1, 2)

    then:
    1 * carRepository.findById(_) >> Optional.empty()
    EntityNotFoundException exception = thrown()
    assert exception.message == "Car does not exist for id: 2"
  }

  def "Throw CarAlreadyInUseException for a car that has already been selected"(){
    given:
    CarDO carDo = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC,"Mazda")
    carDo.setSelected(true)
    DriverDO driverDO = new DriverDO("Paul", "45678")
    driverDO.setOnlineStatus(OnlineStatus.ONLINE)
    driverDO.setCar(null)

    when:
    driverService.selectCar(1, 2)

    then:
    1 * driverRepository.findById(_) >> Optional.of(driverDO)
    1 * carRepository.findById(_) >> Optional.of(carDo)
    CarAlreadyInUseException exception = thrown()
    assert exception.message == "Car has already been selected by another driver"
  }

  def "Deselect car for a driver"(){
    given:
    CarDO carDo = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC,"Mazda")
    carDo.setSelected(true)
    DriverDO driverDO = new DriverDO("Paul", "45678")
    driverDO.setCar(carDo)
    DriverDO driverDOAfterDeselected = new DriverDO("Paul", "45678")

    when:
    DriverDO result = driverService.deselectCar(1)

    then:
    1 * driverRepository.findById(_) >> Optional.of(driverDO)
    1 * driverRepository.save(_) >> driverDOAfterDeselected
    assert result
    assert result.car == null
  }

  def "Throw DriverNoCarSelectedException when a driver is deselecting and has no car selected"(){
    given:
    DriverDO driverDO = new DriverDO("Paul", "45678")

    when:
    driverService.deselectCar(1)

    then:
    1 * driverRepository.findById(_) >> Optional.of(driverDO)
    DriverNoCarSelectedException exception = thrown()
    assert exception.message == "Driver has not previously selected a car"
  }
}
