package net.andreweast.api;

import net.andreweast.model.Lecturer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "lecturer", path = "lecturer") // I believe the purpose of this is to skip the need for a separate @RestController. Contrast to: https://spring.io/guides/tutorials/bookmarks/
public interface LecturerRepository extends PagingAndSortingRepository<Lecturer, Long> {
    List<Lecturer> findByName(@Param("name") String name);
//    List<Course> findByDepartmentId(@Param("id") long id);
}
