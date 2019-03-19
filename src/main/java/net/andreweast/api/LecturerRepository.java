package net.andreweast.api;

import net.andreweast.model.Lecturer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "lecturer", path = "lecturer")
public interface LecturerRepository extends PagingAndSortingRepository<Lecturer, Long> {
    @RestResource(path = "name", rel = "name")
    List<Lecturer> findByName(String name);

    @RestResource(path = "department", rel = "department")
    List<Lecturer> findByDepartment_DepartmentId(Long id);
    @RestResource(path = "departmentName", rel = "departmentName")
    List<Lecturer> findByDepartment_Name(String name);

    @RestResource(path = "module", rel = "module")
    List<Lecturer> findByModules_ModuleId_OrModules_Name(Long id, String name);
}
