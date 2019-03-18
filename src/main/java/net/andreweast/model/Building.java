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

@TypeDef(name = "type", typeClass = PGPointType.class)
@Entity
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

    // TODO: maybe have to make a custom type for PGpoint? https://stackoverflow.com/a/53754866/5271224
    // @Basic
//    @Basic
//    @Column(name = "location", nullable = true)
//    private PGpoint location;

    @Type(type = "type")
    private PGpoint location;

//    // See: https://www.baeldung.com/jpa-many-to-many
//    // For a basic ManyToMany relation, where the join table has no extra data in it
//    @ManyToMany(mappedBy = "buildings")
//    private List<Department> departments;

    @OneToMany(mappedBy = "building")
    private List<DepartmentBuilding> departmentBuildings;

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

    //    public PGpoint getLocation() {
//        return location;
//    }
//
//    public void setLocation(PGpoint location) {
//        this.location = location;
//    }

    public List<DepartmentBuilding> getDepartmentBuildings() {
        return departmentBuildings;
    }

    public void setDepartmentBuildings(List<DepartmentBuilding> departmentBuildings) {
        this.departmentBuildings = departmentBuildings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return buildingId.equals(building.buildingId) &&
                name.equals(building.name) &&
                Objects.equals(departmentBuildings, building.departmentBuildings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildingId, name, departmentBuildings);
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Building building = (Building) o;
//        return buildingId.equals(building.buildingId) &&
//                name.equals(building.name) &&
//                Objects.equals(location, building.location) &&
//                Objects.equals(departmentBuildings, building.departmentBuildings);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(buildingId, name, location, departmentBuildings);
//    }
}
