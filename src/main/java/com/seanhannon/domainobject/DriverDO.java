package com.seanhannon.domainobject;

import com.seanhannon.domainvalue.GeoCoordinate;
import com.seanhannon.domainvalue.OnlineStatus;

import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(
  name = "driver",
  uniqueConstraints = @UniqueConstraint(name = "uc_username", columnNames = {"username"})
)
public class DriverDO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private ZonedDateTime dateCreated = ZonedDateTime.now();

  @Column(nullable = false)
  @NotNull(message = "Username can not be null!")
  private String username;

  @Column(nullable = false)
  @NotNull(message = "Password can not be null!")
  private String password;

  @Column(nullable = false)
  private Boolean deleted = false;

  @Embedded
  private GeoCoordinate coordinate;

  @Column
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private ZonedDateTime dateCoordinateUpdated = ZonedDateTime.now();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OnlineStatus onlineStatus;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinTable(name = "driver_with_car",
    joinColumns = {@JoinColumn(name = "driver_id")},
    inverseJoinColumns = {@JoinColumn(name = "car_id")})
  private CarDO car;

  private DriverDO() {
  }


  public DriverDO(String username, String password) {
    this.username = username;
    this.password = password;
    this.deleted = false;
    this.coordinate = null;
    this.dateCoordinateUpdated = null;
    this.onlineStatus = OnlineStatus.OFFLINE;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public OnlineStatus getOnlineStatus() {
    return onlineStatus;
  }

  public void setOnlineStatus(OnlineStatus onlineStatus) {
    this.onlineStatus = onlineStatus;
  }

  public GeoCoordinate getCoordinate() {
    return coordinate;
  }

  public void setCoordinate(GeoCoordinate coordinate) {
    this.coordinate = coordinate;
    this.dateCoordinateUpdated = ZonedDateTime.now();
  }

  public CarDO getCar() {
    return car;
  }

  public void setCar(CarDO car) {
    this.car = car;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DriverDO driverDO = (DriverDO) o;
    return Objects.equals(id, driverDO.id) &&
      Objects.equals(dateCreated, driverDO.dateCreated) &&
      Objects.equals(username, driverDO.username) &&
      Objects.equals(password, driverDO.password) &&
      Objects.equals(deleted, driverDO.deleted) &&
      Objects.equals(coordinate, driverDO.coordinate) &&
      Objects.equals(dateCoordinateUpdated, driverDO.dateCoordinateUpdated) &&
      onlineStatus == driverDO.onlineStatus;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateCreated, username, password, deleted, coordinate, dateCoordinateUpdated, onlineStatus);
  }
}
