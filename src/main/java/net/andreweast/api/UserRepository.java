package net.andreweast.api;

import net.andreweast.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @RestResource(path = "username", rel="username")
    List<User> findByUsername(String username);

    @RestResource(path = "email", rel="email")
    List<User> findByEmail(String email);

    @RestResource(path="department", rel="department")
    List<User> findByDepartment_DepartmentId_OrDepartment_Name(Long id, String name);
}
