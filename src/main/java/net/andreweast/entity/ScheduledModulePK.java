package net.andreweast.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ScheduledModulePK implements Serializable {
    private int scheduleId;
    private int moduleId;

    @Id
    @Column(name = "schedule_id", nullable = false)
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Id
    @Column(name = "module_id", nullable = false)
    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledModulePK that = (ScheduledModulePK) o;
        return scheduleId == that.scheduleId &&
                moduleId == that.moduleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, moduleId);
    }
}
