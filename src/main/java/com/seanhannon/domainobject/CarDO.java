package com.seanhannon.domainobject;


import com.seanhannon.domainvalue.EngineType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
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
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
  name = "car",
  uniqueConstraints = @UniqueConstraint(name = "uc_licensePlate", columnNames = {"licensePlate"})
)
public class CarDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();
    @Column(nullable = false)
    private String licensePlate;
    @Column(nullable = false)
    private int seatCount;
    @Column(nullable = false)
    private boolean convertible;
    @Column(nullable = false)
    private int rating;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EngineType engineType;
    @Column(nullable = false)
    private String manufacturer;
    @Column(nullable = false)
    private boolean selected;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "driver_with_car",
      joinColumns = { @JoinColumn(name = "car_id")},
      inverseJoinColumns = { @JoinColumn(name = "driver_id")})
    private DriverDO driver;

    public CarDO() {
    }

    public CarDO(String licensePlate, int seatCount, boolean convertible, int rating, EngineType engineType,
                 String manufacturer) {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
        this.selected = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public boolean isConvertible() {
        return convertible;
    }

    public void setConvertible(boolean convertible) {
        this.convertible = convertible;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DriverDO getDriver() {
        return driver;
    }

    public void setDriver(DriverDO driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDO carDO = (CarDO) o;
        return getSeatCount() == carDO.getSeatCount() &&
          isConvertible() == carDO.isConvertible() &&
          getRating() == carDO.getRating() &&
          isSelected() == carDO.isSelected() &&
          Objects.equals(getId(), carDO.getId()) &&
          Objects.equals(getDateCreated(), carDO.getDateCreated()) &&
          Objects.equals(getLicensePlate(), carDO.getLicensePlate()) &&
          getEngineType() == carDO.getEngineType() &&
          Objects.equals(getManufacturer(), carDO.getManufacturer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDateCreated(), getLicensePlate(), getSeatCount(), isConvertible(), getRating(), getEngineType(), getManufacturer(), isSelected());
    }
}
