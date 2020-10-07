package com.seanhannon.controller

import com.seanhannon.dataaccessobject.CarRepository
import com.seanhannon.dataaccessobject.DriverRepository
import com.seanhannon.datatransferobject.DriverDTO
import com.seanhannon.domainobject.CarDO
import com.seanhannon.domainobject.DriverDO
import com.seanhannon.domainvalue.EngineType
import com.seanhannon.domainvalue.OnlineStatus
import com.seanhannon.exception.IllegalSearchException
import com.seanhannon.model.JwtRequest
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DriverControllerSpecIT extends Specification {

  @Autowired
  DriverRepository driverRepository

  @Autowired
  CarRepository carRepository

  @Autowired
  TestRestTemplate testRestTemplate

  JsonSlurper jsonSlurper = new JsonSlurper()

  def setup() {
    driverRepository.deleteAll()
    carRepository.deleteAll()
  }

  def "Select a car for a driver"(){
    given:
    DriverDO driverDave = new DriverDO("DriverDave", "DriverDavePW")
    driverDave.setOnlineStatus(OnlineStatus.ONLINE)
    DriverDO savedDriver = driverRepository.save(driverDave)
    CarDO carDO = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "VW")
    CarDO savedCar = carRepository.save(carDO)
    String token = authenticateUser()

    when:
    HttpHeaders httpHeaders = new HttpHeaders()
    httpHeaders.set("Authorization", "Bearer "+token)
    httpHeaders.setContentType(MediaType.APPLICATION_JSON)
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
    String url = "/v1/drivers/" + savedDriver.id + "/car/" + savedCar.id
    def result = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(httpHeaders), DriverDTO.class)

    then:
    assert result
    assert result.statusCode == HttpStatus.CREATED
    assert result.hasBody()
    DriverDTO driverDTO = result.body
    assert driverDTO.carId == savedCar.id
    assert driverDTO.username == savedDriver.username
  }

  def "Deselect car for driver"(){
    given:
    DriverDO driverDave = new DriverDO("DriverDave", "DriverDavePW")
    driverDave.setOnlineStatus(OnlineStatus.ONLINE)
    DriverDO savedDriver = driverRepository.save(driverDave)
    CarDO carDO = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "VW")
    CarDO savedCar = carRepository.save(carDO)
    String token = authenticateUser()

    HttpHeaders httpHeaders = new HttpHeaders()
    httpHeaders.set("Authorization", "Bearer "+token)
    httpHeaders.setContentType(MediaType.APPLICATION_JSON)
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
    String url = "/v1/drivers/" + savedDriver.id + "/car/" + savedCar.id
    testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(httpHeaders), DriverDTO.class)

    when:
    String deselectUrl = "/v1/drivers/" + savedDriver.id + "/car"
    def result = testRestTemplate.exchange(deselectUrl, HttpMethod.PUT, new HttpEntity<>(httpHeaders), DriverDTO.class)

    then:
    assert result
    assert result.statusCode == HttpStatus.OK
    assert result.hasBody()
    DriverDTO driverDTO = result.getBody()
    assert driverDTO.carId == null
  }

  def "Find all ONLINE Drivers"(){
    given:
    DriverDO driverDave = new DriverDO("DriverDave", "DriverDavePW")
    driverDave.setOnlineStatus(OnlineStatus.ONLINE)
    driverRepository.save(driverDave)
    DriverDO driverJane = new DriverDO("DriverJane", "DriverJanePW")
    driverRepository.save(driverJane)
    String token = authenticateUser()

    when:
    HttpHeaders httpHeaders = new HttpHeaders()
    httpHeaders.set("Authorization", "Bearer "+token)
    httpHeaders.setContentType(MediaType.APPLICATION_JSON)
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
    def result = testRestTemplate.exchange("/v1/drivers?search=onlineStatus==ONLINE", HttpMethod.GET, new HttpEntity<>(httpHeaders), DriverDTO[].class)

    then:
    assert result.statusCode == HttpStatus.OK

    def drivers = result.body
    assert drivers.size() == 1

    DriverDTO driver = drivers.first()
    assert driver.username == driverDave.username
    assert driver.password == driverDave.password
  }

  def "Search for driver with a car with a certain licensePlate"(){
    given:
    CarDO carDO = new CarDO("123456", 5, false, 5, EngineType.ELECTRIC, "VW")
    carDO.setSelected(true)
    carRepository.save(carDO)
    List<CarDO> cars = carRepository.findAll()
    CarDO savedCar = cars.first()
    DriverDO driverDave = new DriverDO("DriverDave", "DriverDavePW")
    driverDave.setOnlineStatus(OnlineStatus.ONLINE)
    driverDave.setCar(savedCar)
    driverRepository.save(driverDave)
    String token = authenticateUser()


    when:
    HttpHeaders httpHeaders = new HttpHeaders()
    httpHeaders.set("Authorization", "Bearer "+token)
    httpHeaders.setContentType(MediaType.APPLICATION_JSON)
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
    def result = testRestTemplate.exchange("/v1/drivers?search=car.licensePlate==123456", HttpMethod.GET, new HttpEntity<>(httpHeaders), DriverDTO[].class)

    then:
    assert result.statusCode == HttpStatus.OK

    def drivers = result.body
    assert drivers.size() == 1

    DriverDTO driver = drivers.first()
    assert driver.username == driverDave.username
    assert driver.password == driverDave.password
    assert driver.carId == savedCar.id
  }

  def "Search for term that is not on objects"(){
    given:
    DriverDO driverDave = new DriverDO("DriverDave", "DriverDavePW")
    driverDave.setOnlineStatus(OnlineStatus.ONLINE)
    driverRepository.save(driverDave)
    String token = authenticateUser()

    when:
    HttpHeaders httpHeaders = new HttpHeaders()
    httpHeaders.set("Authorization", "Bearer "+token)
    httpHeaders.setContentType(MediaType.APPLICATION_JSON)
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
    def result = testRestTemplate.exchange("/v1/drivers?search=test==test", HttpMethod.GET, new HttpEntity<>(httpHeaders), IllegalSearchException.class)

    then:
    assert result
    assert result.statusCode == HttpStatus.BAD_REQUEST
    IllegalSearchException exception = result.body
    assert exception
    assert exception.message == "Search request is invalid, search request: test==test"
  }

  def authenticateUser(){
    JwtRequest jwtRequest = new JwtRequest("employee", "password")
    ResponseEntity authResponse = testRestTemplate.postForEntity("/authenticate", jwtRequest, String.class)
    String token = jsonSlurper.parseText(authResponse.body)["token"]
    token
  }
}
