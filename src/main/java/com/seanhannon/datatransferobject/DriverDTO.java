package com.seanhannon.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seanhannon.domainvalue.GeoCoordinate;
import com.seanhannon.domainvalue.OnlineStatus;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDTO {
  @JsonIgnore
  private Long id;

  @NotNull(message = "Username can not be null!")
  private String username;

  @NotNull(message = "Password can not be null!")
  private String password;

  private GeoCoordinate coordinate;

  private OnlineStatus onlineStatus;

  private Long carId;

  private DriverDTO() {
  }

  private DriverDTO(Long id, String username, String password, GeoCoordinate coordinate) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.coordinate = coordinate;
    this.carId = null;
  }

  private DriverDTO(Long id, String username, String password, GeoCoordinate coordinate, Long carId) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.coordinate = coordinate;
    this.carId = carId;
  }

  private DriverDTO(Long id, String username, String password, GeoCoordinate coordinate, Long carId, OnlineStatus onlineStatus) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.coordinate = coordinate;
    this.carId = carId;
    this.onlineStatus = onlineStatus;
  }

  public static DriverDTOBuilder newBuilder() {
    return new DriverDTOBuilder();
  }

  @JsonProperty
  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public GeoCoordinate getCoordinate() {
    return coordinate;
  }

  public Long getCarId() {
    return carId;
  }

  public OnlineStatus getOnlineStatus() {
    return onlineStatus;
  }

  public static class DriverDTOBuilder {
    private Long id;
    private String username;
    private String password;
    private GeoCoordinate coordinate;
    private Long carId;
    private OnlineStatus onlineStatus;

    public DriverDTOBuilder setId(Long id) {
      this.id = id;
      return this;
    }

    public DriverDTOBuilder setUsername(String username) {
      this.username = username;
      return this;
    }

    public DriverDTOBuilder setPassword(String password) {
      this.password = password;
      return this;
    }

    public DriverDTOBuilder setCoordinate(GeoCoordinate coordinate) {
      this.coordinate = coordinate;
      return this;
    }

    public DriverDTOBuilder setCarId(Long carId) {
      this.carId = carId;
      return this;
    }

    public DriverDTOBuilder setOnlineStatus(OnlineStatus onlineStatus){
      this.onlineStatus = onlineStatus;
      return this;
    }

    public DriverDTO createDriverDTO() {
      return new DriverDTO(id, username, password, coordinate, carId, onlineStatus);
    }
  }
}
