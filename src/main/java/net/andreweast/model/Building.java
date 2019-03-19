package net.andreweast.model;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.postgresql.geometric.PGpoint;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@TypeDef(name = "CustomPGPoint", typeClass = PGPointType.class)
@Table(name = "buildings", schema = "public", catalog = "ga_dev")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "building_generator")
    @SequenceGenerator(name = "building_generator", sequenceName = "building_id_sequence", allocationSize = 1)
    @Column(name = "building_id", updatable = false, nullable = false)
    private Long buildingId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Type(type = "CustomPGPoint") // Custom PGPointType class, see: https://stackoverflow.com/a/53754866/5271224
    private PGpoint location;

    // This expresses a Many-To-Many relationship, but the case where the JoinTable has data fields that are important
    // See: https://www.baeldung.com/jpa-many-to-many
    // For a basic @ManyToMany relation, where the join table has no extra data in it, see: https://www.baeldung.com/jpa-many-to-many
    @OneToMany(mappedBy = "building")
    private List<DepartmentBuilding> departmentBuildings;

    @OneToMany(mappedBy = "building")
    private List<Venue> venues;

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PGpoint getLocation() {
        return location;
    }

    public void setLocation(PGpoint location) {
        this.location = location;
    }

    public List<DepartmentBuilding> getDepartmentBuildings() {
        return departmentBuildings;
    }

    public void setDepartmentBuildings(List<DepartmentBuilding> departmentBuildings) {
        this.departmentBuildings = departmentBuildings;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return buildingId.equals(building.buildingId) &&
                name.equals(building.name) &&
                Objects.equals(location, building.location) &&
                Objects.equals(departmentBuildings, building.departmentBuildings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildingId, name, location, departmentBuildings);
    }
}
