package net.andreweast.services.data.api;

import net.andreweast.services.data.model.ScheduledModule;
import net.andreweast.services.data.model.ScheduledModulePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "scheduledModule", path = "scheduledModule")
//public interface ScheduledModuleRepository extends PagingAndSortingRepository<ScheduledModule, ScheduledModulePK> {
//    @RestResource(path = "schedule", rel = "schedule")
//    List<ScheduledModule> findBySchedule_ScheduleId(Long id);

@Repository
//@Transactional(readOnly = true)
//public class ScheduledModuleRepository {
public interface ScheduledModuleRepository extends JpaRepository<ScheduledModule, ScheduledModulePK> {
//    @PersistenceContext
//    private EntityManager em;

    List<ScheduledModule> getAllBySchedule_ScheduleId(Long scheduleId);

    /**
     * "Upsert": Inserts new or updates existing (if scheduleId/moduleId record already exists)
     * @return number of modified rows
     */
    @Modifying
//    @Query(value = "UPDATE scheduled_module s SET s.timeslot_id = :timeslotId, s.venue_id = :vneueId where s.schedule_id = :scheduleId AND s.module_id = :moduleId")
    @Query(value = "INSERT INTO scheduled_modules (schedule_id, module_id, timeslot_id, venue_id) VALUES (:scheduleId, :moduleId, :timeslotId, :venueId) ON CONFLICT (schedule_id, module_id) DO UPDATE SET timeslot_id = :timeslotId, venue_id = :venueId",
            nativeQuery = true)
    @Transactional
    int upsert(@Param("scheduleId") Long scheduleId, @Param("moduleId") Long moduleId, @Param("timeslotId") Long timeslotId, @Param("venueId") Long venueId);

//    public void update(long scheduleId, long moduleId, long timeslotId, long venueId) {
//        Query query = em.createQuery("UPDATE ScheduledModule s SET s.timeslot_id = :timeslotId, s.venue_id =  ?2 where s.schedule_id = ?3 AND s.module_id = ?4")
//                .setParameter("timeslotId", timeslotId)
//                .setParameter(2, venueId) // TODO
//                .setParameter(3, scheduleId) // TODO
//                .setParameter(4, moduleId); // TODO
//        query.getSingleResult();
//    }
}

// DEBUG: note: SELECT DISTINCT a FROM Author a INNER JOIN a.books b WHERE b.publisher.name = 'XYZ Press'

/*
@Query("SELECT u FROM User u WHERE u.status = :status and u.name = :name")
User findUserByUserStatusAndUserName(@Param("status") Integer userStatus,
  @Param("name") String userName);
 */
