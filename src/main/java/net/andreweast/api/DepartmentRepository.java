package net.andreweast.api;

import net.andreweast.model.Department;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "department", path = "department") // I believe the purpose of this is to skip the need for a separate @RestController. Contrast to: https://spring.io/guides/tutorials/bookmarks/
public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long> {
//    List<Course> findByCourseId(@Param("id") int id);
    List<Department> findByName(@Param("name") String name);
}
