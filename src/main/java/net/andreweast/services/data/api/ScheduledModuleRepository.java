package net.andreweast.services.data.api;

import net.andreweast.services.data.model.ScheduledModule;
import net.andreweast.services.data.model.ScheduledModulePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "scheduledModule", path = "scheduledModule")
public interface ScheduledModuleRepository extends JpaRepository<ScheduledModule, ScheduledModulePK> {
    @RestResource(path = "schedule", rel = "schedule")
    List<ScheduledModule> findBySchedule_ScheduleId_OrderByTimeslot_TimeslotIdAsc(Long id);

    /**
     * "Upsert": Inserts new or updates existing (if scheduleId/moduleId record already exists)
     * See: https://www.postgresql.org/docs/9.6/sql-insert.html
     *
     * @return number of modified rows
     */
    @Modifying
    @Query(value = "INSERT INTO scheduled_modules (schedule_id, module_id, timeslot_id, venue_id) " +
            "VALUES (:scheduleId, :moduleId, :timeslotId, :venueId) " +
            "ON CONFLICT (schedule_id, module_id) DO UPDATE SET timeslot_id = :timeslotId, venue_id = :venueId",
            nativeQuery = true)
    @Transactional
    int upsert(@Param("scheduleId") Long scheduleId, @Param("moduleId") Long moduleId, @Param("timeslotId") Long timeslotId, @Param("venueId") Long venueId);
}

// DEBUG: note: SELECT DISTINCT a FROM Author a INNER JOIN a.books b WHERE b.publisher.name = 'XYZ Press'

/*
DEBUG: note:
@Query("SELECT u FROM User u WHERE u.status = :status and u.name = :name")
User findUserByUserStatusAndUserName(@Param("status") Integer userStatus,
  @Param("name") String userName);
 */
