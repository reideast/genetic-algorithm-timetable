package net.andreweast.services.data.api;

import net.andreweast.services.data.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @RestResource(path = "username", rel = "username")
    List<User> findByUsername(String username);

    User findDistinctByUsername(String username);

    @RestResource(path = "email", rel = "email")
    List<User> findByEmail(String email);

    @RestResource(path = "department", rel = "department")
    List<User> findByDepartment_DepartmentId(Long id);
    @RestResource(path = "departmentName", rel = "departmentName")
    List<User> findByDepartment_Name(String name);

    @RestResource(path = "schedule", rel = "schedule")
    List<User> findBySchedules_ScheduleId(Long id);
}
