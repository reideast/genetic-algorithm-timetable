package net.andreweast.services.data.model;

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

    public ScheduledModulePK() {
    }

    public ScheduledModulePK(Long scheduleId, Long moduleId) {
        this.scheduleId = scheduleId;
        this.moduleId = moduleId;
    }

    /**
     * Supports reversing one of these PKs that was created by the toString() method of this class
     *
     * @param pkCombinedByUnderscore
     */
    public ScheduledModulePK(String pkCombinedByUnderscore) {
        String[] keys = pkCombinedByUnderscore.split("_");
        this.scheduleId = Long.parseLong(keys[0]);
        this.moduleId = Long.parseLong(keys[1]);
    }

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

    /**
     * Serialize this composite primary key into the two IDs joined by an underscore
     *
     * @return scheduleId then moduleId
     */
    @Override
    public String toString() {
        return scheduleId + "_" + moduleId;
    }
}
