package net.andreweast.services.data.api;

import net.andreweast.services.data.model.Timeslot;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "timeslots", path = "timeslots")
public interface TimeslotRepository extends PagingAndSortingRepository<Timeslot, Long> {
    @RestResource(path = "day", rel = "day")
    List<Timeslot> findByDay(Integer day);

    @RestResource(path = "lecturerPreference", rel = "lecturerPreference")
    List<Timeslot> findByLecturerTimeslotPreferences_Lecturer_LecturerId(Long id);
    @RestResource(path = "lecturerPreferenceName", rel = "lecturerPreferenceName")
    List<Timeslot> findByLecturerTimeslotPreferences_Lecturer_Name(String name);
}
