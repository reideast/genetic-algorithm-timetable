package net.andreweast.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class DepartmentBuildingEntityPK implements Serializable {
    private int departmentId;
    private int buildingId;

    @Column(name = "department_id", nullable = false)
    @Id
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Column(name = "building_id", nullable = false)
    @Id
    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentBuildingEntityPK that = (DepartmentBuildingEntityPK) o;
        return departmentId == that.departmentId &&
                buildingId == that.buildingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, buildingId);
    }
}
