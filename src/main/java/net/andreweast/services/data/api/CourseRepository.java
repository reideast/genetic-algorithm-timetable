package net.andreweast.services.data.api;

import net.andreweast.services.data.model.Course;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

// See: https://spring.io/guides/gs/accessing-data-rest/
// Also includes details on what GET/POST/PATCH/DELETE methods work
// /course gets all
// /course/{ID_NUM} gets one by id
// /course/search/findByName?name={NAME} will find one, by name

/*
ManyToOne relationship
See: https://www.baeldung.com/spring-data-rest-relationships
Add a new course with department link already set up:
POST /course, body JSON:
{
    "name": "4LAW",
    "department": "http://localhost:5000/department/3"
}

Add a new course with NULL department
POST /course, body JSON:
{
    "name": "4LAW"
}
Add the department link after-the-fact:
PUT /course/{COURSE_ID}/department
Content-Type:text/uri-list
body: "http://localhost:5000/department/3"
*/

@RepositoryRestResource(collectionResourceRel = "course", path = "course")
// I believe the purpose of this is to skip the need for a separate @RestController. Contrast to: https://spring.io/guides/tutorials/bookmarks/
public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
    // /course/{ID} is created implicitly
    // All */search/* queries go here:

    // Without @RestResource annotation: List<Course> findByDepartmentId(@Param("id") long id);
    @RestResource(path = "name", rel = "name")
    List<Course> findByName(String name);

    @RestResource(path = "department", rel = "department")
    List<Course> findByDepartment_DepartmentId(Long id);
    @RestResource(path = "departmentName", rel = "departmentName")
    List<Course> findByDepartment_Name(String name);

    @RestResource(path = "module", rel = "module")
    List<Course> findByCourseModules_Module_ModuleId(Long id);
    @RestResource(path = "moduleName", rel = "moduleName")
    List<Course> findByCourseModules_Module_Name(String name);
    // This seems to work, too:
    // @RestResource(path="module", rel="module")
    // List<Course> findByCourseModules_Module_ModuleId_OrCourseModules_Module_Name(Long id, String name);
}
