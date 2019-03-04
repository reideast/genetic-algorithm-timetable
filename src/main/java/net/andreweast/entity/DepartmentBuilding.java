package net.andreweast.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "department_building", schema = "public", catalog = "ga_dev")
@IdClass(DepartmentBuildingPK.class)
public class DepartmentBuilding {
    private int departmentId;
    private int buildingId;
    private Integer score;
//    private Building buildingsByBuildingId;

    @Id
    @Column(name = "department_id", nullable = false)
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Id
    @Column(name = "building_id", nullable = false)
    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    @Basic
    @Column(name = "score", nullable = true)
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentBuilding that = (DepartmentBuilding) o;
        return departmentId == that.departmentId &&
                buildingId == that.buildingId &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, buildingId, score);
    }

//    @ManyToOne
//    @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false)
//    public Building getBuildingByBuildingId() {
//        return buildingsByBuildingId;
//    }
//
//    public void setBuildingByBuildingId(Building buildingsByBuildingId) {
//        this.buildingsByBuildingId = buildingsByBuildingId;
//    }
}
