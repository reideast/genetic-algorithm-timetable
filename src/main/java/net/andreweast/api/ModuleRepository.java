package net.andreweast.api;

import net.andreweast.model.Module;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "module", path = "module")
public interface ModuleRepository extends PagingAndSortingRepository<Module, Long> {
    @RestResource(path = "name", rel="name")
    List<Module> findByName(String name);

    @RestResource(path="lecturer", rel="lecturer")
    List<Module> findByLecturer_LecturerId(Long id);
    @RestResource(path="lecturerName", rel="lecturerName")
    List<Module> findByLecturer_Name(String name);

    @RestResource(path="course", rel="course")
    List<Module> findByCourseModules_CourseCourseId(Long id);
    @RestResource(path="courseName", rel="courseName")
    List<Module> findByCourseModules_CourseName(String name);

    /*
     * This is the key REST call for getting the details of a schedule
     * Gets ALL the modules for a certain schedule (by schedule_id)
     * Those can then be queried to find details
     * TODO: make one custom SQL query: input schedule ID; output: module name + ID, timeslots, venue name
     * TODO: other related query: same, but limited to one Course's timetable
     */
    @RestResource(path="scheduledModule", rel="scheduledModule")
    List<Module> findByScheduledModules_Schedule_ScheduleId(Long id);
}
