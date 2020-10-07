package com.seanhannon.service.car

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.seanhannon.dataaccessobject.CarRepository
import com.seanhannon.domainobject.CarDO
import com.seanhannon.domainvalue.EngineType
import com.seanhannon.exception.ConstraintsViolationException
import com.seanhannon.exception.EntityNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification

class DefaultCarServiceSpec extends Specification{
    @Subject
    DefaultCarService carService

    @Collaborator
    CarRepository carRepository = Mock()

    def "Create new car"(){
        given:
        CarDO carDO = new CarDO("123456", 5, false, 3, EngineType.GAS, "VW")

        when:
        CarDO carDOResult = carService.create(carDO)

        then:
        1 * carRepository.save(_) >> carDO
        assert carDOResult
        assert carDOResult.manufacturer == "VW"
        assert carDOResult.licensePlate == "123456"
        assert carDOResult.seatCount == 5
        assert carDOResult.convertible == false
        assert carDOResult.rating == 3
        assert carDOResult.engineType == EngineType.GAS
    }

    def "ConstraintsViolationException for an attempt to make a car with the same license plate"(){
        when:
        carService.create(new CarDO())

        then:
        1 * carRepository.save(_) >> {throw new DataIntegrityViolationException("test")}
        ConstraintsViolationException exception = thrown()
        assert exception
        assert exception.message == "test"
    }

    def "Find a car"(){
        given:
        CarDO carDO = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "VW")

        when:
        CarDO car = carService.find(1)

        then:
        1 * carRepository.findById(_) >> Optional.of(carDO)
        assert car
    }

    def "EntityNotFoundException for a car ID that does not exist"(){
        when:
        carService.find(1)

        then:
        1 * carRepository.findById(_) >> Optional.empty()
        EntityNotFoundException entityNotFoundException = thrown()
        assert entityNotFoundException
        assert entityNotFoundException.message == "Unable to find car for id: 1"
    }

    def "Find all cars"(){
        given:
        CarDO carDO = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "VW")
        ArrayList<CarDO> carDOS = new ArrayList<>()
        carDOS.add(carDO)

        when:
        def carDOSResult = carService.findAll()

        then:
        1 * carRepository.findAll() >> carDOS
        assert carDOSResult.size() == 1
    }

    def "Find all cars when none are saved"(){
        when:
        def carDOS = carService.findAll()

        then:
        1 * carRepository.findAll() >> new ArrayList<>()
        assert carDOS.size() == 0
    }

    def "Delete a car"(){
        when:
        carService.delete(1)

        then:
        1 * carRepository.deleteById(_)
    }

    def "Update a car details"(){
        given:
        String licensePlate = "123456"
        int seatCount = 5
        boolean convertible = false
        int rating = 5
        EngineType engineType = EngineType.GAS
        CarDO carDOToUpdate = new CarDO(licensePlate, seatCount, convertible, rating, engineType, "VW")

        when:
        CarDO carDO = carService.update(1, carDOToUpdate)

        then:
        1 * carRepository.findById(_) >> Optional.of(carDOToUpdate)
        1 * carRepository.save(_) >> carDOToUpdate
        assert carDO
    }

    def "EntityNotFoundException for a car ID that does not exist when trying to update a car"(){
        when:
        carService.update(1, new CarDO())

        then:
        1 * carRepository.findById(_) >> Optional.empty()
        EntityNotFoundException entityNotFoundException = thrown()
        assert entityNotFoundException
        assert entityNotFoundException.message == "Car does not exist for id: 1"
    }
}
