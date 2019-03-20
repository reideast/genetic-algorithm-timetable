package net.andreweast.api;

import net.andreweast.model.Job;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "job", path = "job")
public interface JobRepository extends PagingAndSortingRepository<Job, Long> {
    @RestResource(path = "schedule", rel = "schedule")
    List<Job> findBySchedule_ScheduleId(Long id);
}
