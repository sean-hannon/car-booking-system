package com.seanhannon.controller

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.seanhannon.datatransferobject.DriverDTO
import com.seanhannon.domainobject.CarDO
import com.seanhannon.domainobject.DriverDO
import com.seanhannon.domainvalue.EngineType
import com.seanhannon.domainvalue.OnlineStatus
import com.seanhannon.exception.CarAlreadyInUseException
import com.seanhannon.exception.DriverAlreadySelectedACarException
import com.seanhannon.exception.DriverNoCarSelectedException
import com.seanhannon.exception.EntityNotFoundException
import com.seanhannon.exception.IncorrectStatusException
import com.seanhannon.service.driver.DriverService
import spock.lang.Specification

class DriverControllerSpec extends Specification{
  @Subject
  DriverController driverController

  @Collaborator
  DriverService driverService = Mock()

  def "Select car for driver"(){
    given:
    CarDO carDO = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "VW")
    carDO.setId(1)
    DriverDO driverDO = new DriverDO("Dave", "123")
    driverDO.setOnlineStatus(OnlineStatus.ONLINE)
    driverDO.setCar(carDO)
    driverDO.setId(2)

    when:
    DriverDTO driverDTO = driverController.selectCar(2, 1)

    then:
    1 * driverService.selectCar(2, 1) >> driverDO
    assert driverDTO
  }

  def "Throw EntityNotFoundException for driverId that cannot be found"(){
    given:
    1 * driverService.selectCar(_, _) >> {throw new EntityNotFoundException("Unable to find driver")}

    when:
    driverController.selectCar(1, 2)

    then:
    EntityNotFoundException exception = thrown()
    assert exception.message == "Unable to find driver"
  }

  def "OFFLINE driver try's to select car"(){
    given:
    1 * driverService.selectCar(_,_) >> {throw new IncorrectStatusException("Incorrect status")}

    when:
    driverController.selectCar(1, 2)

    then:
    IncorrectStatusException exception = thrown()
    assert exception.message == "Incorrect status"
  }

  def "Driver already has a car selected and try's to select another"(){
    given:
    1 * driverService.selectCar(_, _) >> {throw new DriverAlreadySelectedACarException("Driver has already selected a car")}

    when:
    driverController.selectCar(1, 2)

    then:
    DriverAlreadySelectedACarException exception = thrown()
    assert exception.message == "Driver has already selected a car"

  }

  def "Driver try's to select a car that does not exist"(){
    given:
    1 * driverService.selectCar(_, _) >> {throw new EntityNotFoundException("Car not found")}

    when:
    driverController.selectCar(1, 2)

    then:
    EntityNotFoundException exception = thrown()
    assert exception.message == "Car not found"
  }

  def "Driver try's to select a car that is already selected"(){
    given:
    1 * driverService.selectCar(_, _) >> {throw new CarAlreadyInUseException("Car already selected by another driver")}

    when:
    driverController.selectCar(1, 2)

    then:
    CarAlreadyInUseException exception = thrown()
    assert exception.message == "Car already selected by another driver"
  }

  def "Deselect car for driver"(){
    given:
    DriverDO driverDO = new DriverDO("Dave", "123")
    driverDO.setOnlineStatus(OnlineStatus.ONLINE)
    driverDO.setCar(new CarDO())
    when:
    DriverDTO driverDTO = driverController.deselectCar(1)

    then:
    1 * driverService.deselectCar(_) >> driverDO
    assert driverDTO
  }

  def "Throw DriverNoCarSelectedException for a driver who has not selected a car yet"(){
    given:
    1 * driverService.deselectCar(_) >> {throw new DriverNoCarSelectedException("No car selected")}

    when:
    driverController.deselectCar(1)

    then:
    DriverNoCarSelectedException exception = thrown()
    assert exception.message == "No car selected"
  }

  def "Throw EntityNotFoundException for unknown driverId"(){
    given:
    1 * driverService.deselectCar(_) >> {throw new EntityNotFoundException("Unable to find driver")}

    when:
    driverController.deselectCar(1)

    then:
    EntityNotFoundException exception = thrown()
    assert exception.message == "Unable to find driver"
  }

  def "Search for a driver with their username"(){
    given:
    DriverDO driverDO = new DriverDO("Dave", "123")
    driverDO.setOnlineStatus(OnlineStatus.ONLINE)
    driverDO.setCar(new CarDO())
    ArrayList<DriverDO> driverDOS = new ArrayList<>()
    driverDOS.add(driverDO)

    when:
    def result = driverController.findAllDriversByQuery("username==Dave")

    then:
    1 * driverService.search(_) >> driverDOS
    assert result.size() == 1
    DriverDTO driverDTO = result.get(0)
    assert driverDTO.username == "Dave"
    assert driverDTO.password == "123"
  }
}
