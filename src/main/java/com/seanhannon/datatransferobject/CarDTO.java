package com.seanhannon.datatransferobject;

import com.seanhannon.domainvalue.EngineType;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CarDTO {
    private Long id;
    @Min(6)
    @Size(min=6, max=12)
    private String licensePlate;
    @Min(2)
    private int seatCount;
    private boolean convertible;
    @Min(0)
    @Max(5)
    private int rating;
    @NotNull(message = "Car requires an engine")
    private EngineType engineType;
    @NotNull(message = "Car requires a manufacturer")
    private String manufacturer;

    private CarDTO(){}

    private CarDTO(Long id, String licensePlate, int seatCount, boolean convertible, int rating, EngineType engineType, String manufacturer) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }

    public static CarDTOBuilder newBuilder()
    {
        return new CarDTOBuilder();
    }

    public static class CarDTOBuilder{
        private Long id;
        private String licensePlate;
        private int seatCount;
        private boolean convertible;
        private int rating;
        private EngineType engineType;
        private String manufacturer;

        public CarDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public CarDTOBuilder setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
            return this;
        }

        public CarDTOBuilder setSeatCount(int seatCount) {
            this.seatCount = seatCount;
            return this;
        }

        public CarDTOBuilder setConvertible(boolean convertible) {
            this.convertible = convertible;
            return this;
        }

        public CarDTOBuilder setRating(int rating) {
            this.rating = rating;
            return this;
        }

        public CarDTOBuilder setEngineType(EngineType engineType) {
            this.engineType = engineType;
            return this;
        }

        public CarDTOBuilder setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public CarDTO createCarDTO() {
            return new CarDTO(id, licensePlate, seatCount, convertible, rating, engineType, manufacturer);
        }
    }
}
