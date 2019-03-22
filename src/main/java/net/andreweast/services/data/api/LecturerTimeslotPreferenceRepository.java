package net.andreweast.services.data.api;

import net.andreweast.services.data.model.LecturerTimeslotPreference;
import net.andreweast.services.data.model.LecturerTimeslotPreferencePK;
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

@RepositoryRestResource(collectionResourceRel = "lecturerTimeslotPreference", path = "lecturerTimeslotPreference")
public interface LecturerTimeslotPreferenceRepository extends JpaRepository<LecturerTimeslotPreference, LecturerTimeslotPreferencePK> {
    @RestResource(path = "timeslot", rel = "timeslot")
    List<LecturerTimeslotPreference> findByTimeslot_TimeslotId(Long id);

    @RestResource(path = "lecturer", rel = "lecturer")
    List<LecturerTimeslotPreference> findByLecturer_LecturerId(Long id);
}
