package com.seanhannon.controller

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.seanhannon.datatransferobject.CarDTO
import com.seanhannon.domainobject.CarDO
import com.seanhannon.domainvalue.EngineType
import com.seanhannon.exception.ConstraintsViolationException
import com.seanhannon.exception.EntityNotFoundException
import com.seanhannon.service.car.CarService
import spock.lang.Specification

class CarControllerSpec extends Specification{

    @Subject
    CarController carController

    @Collaborator
    CarService carService = Mock()

    def "Get list of cars"(){
        given:
        CarDO carDO = new CarDO("123456", 5, false, 3, EngineType.GAS, "VW")
        ArrayList cars = new ArrayList<CarDO>()
        cars.add(carDO)
        1 * carService.findAll() >> cars

        when:
        def carList = carController.getCars()

        then:
        assert carList.size() == 1
        CarDTO carDTO =  carList.get(0)
        assert carDTO.licensePlate == "123456"
        assert carDTO.seatCount == 5
        assert !carDTO.convertible
        assert carDTO.rating == 3
        assert carDTO.engineType == EngineType.GAS
        assert carDTO.manufacturer == "VW"
    }

    def "Get a car given a car id"(){
        given:
        CarDO carDO = new CarDO("123456", 5, false, 3, EngineType.GAS, "VW")
        long carId = 123
        1 * carService.find(carId) >> carDO

        when:
        CarDTO carDTO = carController.getCar(carId)

        then:
        assert carDTO
        assert carDTO.licensePlate == "123456"
        assert carDTO.seatCount == 5
        assert !carDTO.convertible
        assert carDTO.rating == 3
        assert carDTO.engineType == EngineType.GAS
        assert carDTO.manufacturer == "VW"
    }

    def "EntityNotFoundException thrown when trying to find a car for Id that does not exist"(){
        given:
        carService.find(_) >> {throw new EntityNotFoundException("Could not find car")}

        when:
        carController.getCar(123)

        then:
        thrown EntityNotFoundException
    }

    def "Create a new car"(){
        given:
        CarDO carDO = new CarDO("123456", 5, false, 3, EngineType.HYBIRD, "VW")
        carDO.setId(123)
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
        CarDTO carDTO = carDTOBuilder.setId(123).setLicensePlate("123456").setSeatCount(5).setConvertible(false)
                .setRating(3).setEngineType(EngineType.HYBIRD).setManufacturer("VW").createCarDTO()
        1 * carService.create(_) >> carDO

        when:
        CarDTO carDTOResult = carController.createCar(carDTO)

        then:
        assert carDTOResult
        assert carDTOResult.id == carDTO.id
        assert carDTOResult.licensePlate == carDTO.licensePlate
        assert carDTOResult.seatCount == carDTO.seatCount
        assert carDTOResult.convertible == carDTO.convertible
        assert carDTOResult.rating == carDTO.rating
        assert carDTOResult.engineType == carDTO.engineType
        assert carDTOResult.manufacturer == "VW"
    }

    def "ConstraintsViolationException for car creation"(){
        given:
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
        CarDTO carDTO = carDTOBuilder.setId(123).setLicensePlate("123456").setSeatCount(5).setConvertible(false)
                .setRating(3).setEngineType(EngineType.HYBIRD).setManufacturer("VW").createCarDTO()
        1 * carService.create(_) >> {throw new ConstraintsViolationException()}

        when:
        carController.createCar(carDTO)

        then:
        thrown ConstraintsViolationException
    }

    def "Delete a car"(){
        when:
        carController.deleteCar(123)

        then:
        1 * carService.delete(123)
    }

    def "Update a car"(){
        given:
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
        CarDTO carDTO = carDTOBuilder.setId(123).setLicensePlate("123456").setSeatCount(5).setConvertible(false)
                .setRating(3).setEngineType(EngineType.HYBIRD).setManufacturer("Mazda").createCarDTO()
        CarDO carDO = new CarDO("123456", 5, false, 3, EngineType.HYBIRD,
                "Mazda")
        carDO.setId(123)

        when:
        CarDTO carDTOResult = carController.update(123, carDTO)

        then:
        1 * carService.update(_,_) >> carDO
        assert carDTOResult
        assert carDTOResult.id == carDTO.id
        assert carDTOResult.licensePlate == carDTO.licensePlate
        assert carDTOResult.seatCount == carDTO.seatCount
        assert carDTOResult.convertible == carDTO.convertible
        assert carDTOResult.rating == carDTO.rating
        assert carDTOResult.engineType == carDTO.engineType
        assert carDTOResult.manufacturer == carDTO.manufacturer
    }

    def "EntityNotFoundException thrown for unknown carId"(){
        given:
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
        CarDTO carDTO = carDTOBuilder.setId(123).setLicensePlate("123456").setSeatCount(5).setConvertible(false)
                .setRating(3).setEngineType(EngineType.HYBIRD).setManufacturer("Mazda").createCarDTO()
        CarDO carDO = new CarDO("123456", 5, false, 3, EngineType.HYBIRD,
                "Mazda")
        carDO.setId(123)
        1 * carService.update(_,_) >> {throw new EntityNotFoundException("Car does not exist for id: " + 123)}

        when:
        carController.update(123, carDTO)

        then:
        thrown EntityNotFoundException
    }

}
