package com.seanhannon.controller

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.seanhannon.config.JwtTokenUtil
import com.seanhannon.model.JwtRequest
import com.seanhannon.model.JwtResponse
import com.seanhannon.service.jwt.JwtUserDetailsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.userdetails.User
import spock.lang.Specification

class JwtAuthenicationControllerSpec extends Specification{

  @Collaborator
  AuthenticationManager authenticationManager = Mock()

  @Collaborator
  JwtTokenUtil jwtTokenUtil = Mock()

  @Collaborator
  JwtUserDetailsService jwtUserDetailsService = Mock()

  @Subject
  JwtAuthenticationController jwtAuthenticationController

  def "Authenticate request with valid username and password"() {
    given:
    JwtRequest jwtRequest = new JwtRequest("employee", "Test1234")

    when:
    ResponseEntity responseEntity = jwtAuthenticationController.createAuthenticationToken(jwtRequest)

    then:
    1 * jwtUserDetailsService.loadUserByUsername("employee") >> new User(
      "employee", "\$2a\$10\$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",  new ArrayList<>())
    1 * jwtTokenUtil.generateToken(_) >> "Token"
    assert  responseEntity.statusCode == HttpStatus.OK
    assert responseEntity.body
    JwtResponse jwtResponse = responseEntity.body
    assert  jwtResponse.token == "Token"
  }

  def "Authenticate request throws Disabled user exception" () {
    given:
    JwtRequest jwtRequest = new JwtRequest(null, null)

    when:
    jwtAuthenticationController.createAuthenticationToken(jwtRequest)

    then:
    1 * authenticationManager.authenticate(_) >> { throw new DisabledException("User disabled") }
    Exception exception = thrown()
    assert exception
    assert exception.message == "USER_DISABLED"
  }

  def "Authenticate request throws Bad Credentials Exception " () {
    given:
    JwtRequest jwtRequest = new JwtRequest(null, null)

    when:
    jwtAuthenticationController.createAuthenticationToken(jwtRequest)

    then:
    1 * authenticationManager.authenticate(_) >> { throw new BadCredentialsException("Incorrect Credentials") }
    Exception exception = thrown()
    assert exception
    assert exception.message == "INVALID_CREDENTIALS"
  }
}

