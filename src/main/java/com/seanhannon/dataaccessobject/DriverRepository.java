package com.seanhannon.dataaccessobject;

import com.seanhannon.domainobject.DriverDO;
import com.seanhannon.domainvalue.EngineType;
import com.seanhannon.domainvalue.OnlineStatus;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>, JpaSpecificationExecutor<DriverDO> {
    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);

    List<DriverDO> findByCarEngineType(EngineType engineType);
}
