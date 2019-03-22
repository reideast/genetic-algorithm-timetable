package net.andreweast.services.data.api;

import net.andreweast.services.data.model.Module;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "module", path = "module")
public interface ModuleRepository extends PagingAndSortingRepository<Module, Long> {
    @RestResource(path = "name", rel = "name")
    List<Module> findByName(String name);

    @RestResource(path = "lecturer", rel = "lecturer")
    List<Module> findByLecturer_LecturerId(Long id);
    @RestResource(path = "lecturerName", rel = "lecturerName")
    List<Module> findByLecturer_Name(String name);

    @RestResource(path = "course", rel = "course")
    List<Module> findByCourseModules_CourseCourseId(Long id);
    @RestResource(path = "courseName", rel = "courseName")
    List<Module> findByCourseModules_CourseName(String name);

    @RestResource(path = "labs", rel = "labs")
    List<Module> findByIsLabTrue();

    @RestResource(path = "classrooms", rel = "classrooms")
    List<Module> findByIsLabFalse();

    /*
     * This is the key REST call for getting the details of a schedule
     * Gets ALL the modules for a certain schedule (by schedule_id)
     * Those can then be queried to find details
     * TODO: make one custom SQL query: input schedule ID; output: module name + ID, timeslots, venue name
     * TODO: other related query: same, but limited to one Course's timetable
     */
    @RestResource(path = "scheduledModule", rel = "scheduledModule")
    List<Module> findByScheduledModules_Schedule_ScheduleId(Long id);


//    @Query(value = "INSERT INTO scheduled_modules (schedule_id, module_id, timeslot_id, venue_id) " +
//            "VALUES (:scheduleId, :moduleId, :timeslotId, :venueId) " +
//            "ON CONFLICT (schedule_id, module_id) DO UPDATE SET timeslot_id = :timeslotId, venue_id = :venueId",
//            nativeQuery = true)
//    List<Module> (@Param("scheduleId") Long scheduleId, @Param("moduleId") Long moduleId, @Param("timeslotId") Long timeslotId, @Param("venueId") Long venueId);
}
