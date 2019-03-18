package net.andreweast.api;

import net.andreweast.model.Building;
import net.andreweast.model.DepartmentBuilding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/*
Create and edit "location" field in JSON:
"location": {
    "x": 123,
    "y": 456
}
N.b. Can't just set x or y, have to set both at once
*/

@RepositoryRestResource(collectionResourceRel = "building", path = "building")
public interface BuildingRepository extends PagingAndSortingRepository<Building, Long> {
    @RestResource(path = "name", rel="name")
    List<Building> findByName(String name);

    @RestResource(path="department", rel="department")
    List<Building> findByDepartmentBuildings_DepartmentDepartmentId(Long id);
//    List<Building> findByDepartmentBuildings_DepartmentDepartmentId_OrDepartmentBuildings_DepartmentName(Long id, String name);

    @RestResource(path="departmentName", rel="departmentName")
    List<Building> findByDepartmentBuildings_DepartmentName(String name);
}
