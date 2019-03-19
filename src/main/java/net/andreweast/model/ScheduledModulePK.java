package net.andreweast.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ScheduledModulePK implements Serializable {
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(name = "module_id", nullable = false)
    private Long moduleId;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledModulePK that = (ScheduledModulePK) o;
        return scheduleId.equals(that.scheduleId) &&
                moduleId.equals(that.moduleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, moduleId);
    }
}
