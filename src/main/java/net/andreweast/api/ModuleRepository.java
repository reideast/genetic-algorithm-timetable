package net.andreweast.api;

import net.andreweast.model.Lecturer;
import net.andreweast.model.Module;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "module", path = "module")
public interface ModuleRepository extends PagingAndSortingRepository<Module, Long> {
    @RestResource(path = "name", rel="name")
    List<Module> findByName(String name);

    @RestResource(path="lecturer", rel="lecturer")
    List<Module> findByLecturer_LecturerIdOrLecturerName(Long id, String name);
}
