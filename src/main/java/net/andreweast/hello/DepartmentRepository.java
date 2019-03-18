package net.andreweast.hello;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

// See: https://spring.io/guides/gs/accessing-data-rest/
// Also includes details on what GET/POST/PATCH/DELETE methods work
// /course gets all
// /course/{ID_NUM} gets one by id
// /course/search/findByName?name={NAME} will find one, by name

@RepositoryRestResource(collectionResourceRel = "department", path = "department") // I believe the purpose of this is to skip the need for a separate @RestController. Contrast to: https://spring.io/guides/tutorials/bookmarks/
public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long> {
//    List<Course> findByCourseId(@Param("id") int id);
    List<Department> findByName(@Param("name") String name);
}
