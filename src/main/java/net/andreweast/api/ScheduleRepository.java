package net.andreweast.api;

import net.andreweast.model.Schedule;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "schedule", path = "schedule")
public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Long> {
    @RestResource(path = "masterSchedule", rel="masterSchedule")
    List<Schedule> findByIsMasterIsTrue();

    @RestResource(path="creator", rel="creator")
    List<Schedule> findByCreator_UserId_OrCreator_Username(Long id, String username);

    @RestResource(path="creatorLatest", rel="creatorLatest")
    List<Schedule> findTopByCreator_UserIdOrderByCreationDateDesc(Long id);
}
