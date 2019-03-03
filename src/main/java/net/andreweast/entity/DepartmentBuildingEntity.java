package net.andreweast.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "department_building", schema = "public", catalog = "ga_dev")
@IdClass(DepartmentBuildingEntityPK.class)
public class DepartmentBuildingEntity {
    private int departmentId;
    private int buildingId;
    private Integer score;
    private BuildingsEntity buildingsByBuildingId;

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
        DepartmentBuildingEntity that = (DepartmentBuildingEntity) o;
        return departmentId == that.departmentId &&
                buildingId == that.buildingId &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, buildingId, score);
    }

    @ManyToOne
    @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false)
    public BuildingsEntity getBuildingsByBuildingId() {
        return buildingsByBuildingId;
    }

    public void setBuildingsByBuildingId(BuildingsEntity buildingsByBuildingId) {
        this.buildingsByBuildingId = buildingsByBuildingId;
    }
}
