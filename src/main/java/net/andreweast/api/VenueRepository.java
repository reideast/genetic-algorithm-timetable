package net.andreweast.api;

import net.andreweast.model.Venue;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "venue", path = "venue")
public interface VenueRepository extends PagingAndSortingRepository<Venue, Long> {
    @RestResource(path = "name", rel = "name")
    List<Venue> findByName(String name);

    @RestResource(path = "building", rel = "building")
    List<Venue> findByBuilding_BuildingId(Long id);

    @RestResource(path = "buildingName", rel = "buildingName")
    List<Venue> findByBuilding_Name(String name);

    List<Venue> findByCapacityGreaterThanEqual(@Param("capacityLowerBound") Integer capacity);
    List<Venue> findByCapacityLessThanEqual(@Param("capacityUpperBound") Integer capacity);
    List<Venue> findByCapacityBetween(@Param("capacityLowerBound") Integer lowerBound,
                                      @Param("capacityUpperBound") Integer upperBound);

    @RestResource(path = "labs", rel = "labs")
    List<Venue> findByIsLabTrue();

    @RestResource(path = "classrooms", rel = "classrooms")
    List<Venue> findByIsLabFalse();
}
