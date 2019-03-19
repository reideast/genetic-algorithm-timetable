package net.andreweast.api;

import net.andreweast.model.Department;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "department", path = "department") // I believe the purpose of this is to skip the need for a separate @RestController. Contrast to: https://spring.io/guides/tutorials/bookmarks/
public interface DepartmentRepository extends PagingAndSortingRepository<Department, Long> {
    @RestResource(path = "name", rel="name")
    List<Department> findByName(String name);

    @RestResource(path="user", rel="user")
    List<Department> findByUsers_UserId(Long id);
    @RestResource(path="userUsername", rel="userUsername")
    List<Department> findByUsers_Username(String username);

    @RestResource(path="course", rel="course")
    List<Department> findByCourses_CourseId(Long id);
    @RestResource(path="courseName", rel="courseName")
    List<Department> findByCourses_Name(String name);

    @RestResource(path="lecturer", rel="lecturer")
    List<Department> findByLecturers_LecturerId(Long id);
    @RestResource(path="lecturerName", rel="lecturerName")
    List<Department> findByLecturers_Name(String name);

    @RestResource(path="building", rel="building")
    List<Department> findByDepartmentBuildings_BuildingBuildingId(Long id);
    @RestResource(path="buildingName", rel="buildingName")
    List<Department> findByDepartmentBuildings_BuildingName(String name);
}
