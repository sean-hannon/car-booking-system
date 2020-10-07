package com.seanhannon.controller

import com.seanhannon.model.JwtRequest
import groovy.json.JsonSlurper
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtAuthenticationControllerSpecIT extends Specification {

  @Autowired
  TestRestTemplate testRestTemplate

  @Autowired
  Environment environment

  def "Make request to authenticate a valid user"() {
    given:
    JwtRequest jwtRequest = new JwtRequest("employee", "password")
    JsonSlurper jsonSlurper = new JsonSlurper()

    when:
    ResponseEntity response = testRestTemplate.postForEntity("/authenticate", jwtRequest, String.class)

    then:
    assert response.body
    def object = jsonSlurper.parseText(response.body)
    assert object["token"] != null
  }

  def "Make request to authenticate a invalid user"() {
    given:
    CloseableHttpClient client = HttpClients.createDefault()
    String port = environment.getProperty("local.server.port")
    String json = "{\"username\":\"manager\",\"password\":\"TEST1234\"}"
    HttpPost httpPost = new HttpPost("http://localhost:" + port + "/authenticate")
    httpPost.setHeader("Accept", "application/json")
    httpPost.setHeader("Content-type", "application/json")
    httpPost.setEntity(new StringEntity(json))

    when:
    HttpResponse response = client.execute(httpPost)

    then:
    assert response.getStatusLine().getStatusCode() == 401
  }
}