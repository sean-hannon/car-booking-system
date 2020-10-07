package com.seanhannon.dataaccessobject;

import com.seanhannon.domainobject.CarDO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CarRepository extends CrudRepository<CarDO, Long>, JpaSpecificationExecutor<CarDO> {
    Optional<CarDO> findByLicensePlate(String licensePlate);
}
