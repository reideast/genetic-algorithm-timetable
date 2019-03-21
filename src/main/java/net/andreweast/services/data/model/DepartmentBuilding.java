package net.andreweast.services.data.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "department_building", schema = "public", catalog = "ga_dev")
public class DepartmentBuilding {
    @EmbeddedId
    private DepartmentBuildingPK id;

    @ManyToOne
    @MapsId("department_id")
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @MapsId("building_id")
    @JoinColumn(name = "building_id")
    private Building building;

    @Basic
    @Column(name = "score", nullable = true)
    private Integer score;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public DepartmentBuildingPK getId() {
        return id;
    }

    public void setId(DepartmentBuildingPK id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentBuilding that = (DepartmentBuilding) o;
        return id.equals(that.id) &&
                department.equals(that.department) &&
                building.equals(that.building) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, department, building, score);
    }
}
