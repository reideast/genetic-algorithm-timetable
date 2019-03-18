package net.andreweast.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DepartmentBuildingPK implements Serializable {
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "building_id", nullable = false)
    private Long buildingId;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentBuildingPK that = (DepartmentBuildingPK) o;
        return departmentId.equals(that.departmentId) &&
                buildingId.equals(that.buildingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, buildingId);
    }
}
