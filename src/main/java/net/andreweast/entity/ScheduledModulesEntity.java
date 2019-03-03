package net.andreweast.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "scheduled_modules", schema = "public", catalog = "ga_dev")
@IdClass(ScheduledModulesEntityPK.class)
public class ScheduledModulesEntity {
    private ModulesEntity modulesByModuleId;
    private int scheduleId;
    private int moduleId;

    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "module_id", nullable = false)
    public ModulesEntity getModulesByModuleId() {
        return modulesByModuleId;
    }

    public void setModulesByModuleId(ModulesEntity modulesByModuleId) {
        this.modulesByModuleId = modulesByModuleId;
    }

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
        ScheduledModulesEntity that = (ScheduledModulesEntity) o;
        return scheduleId == that.scheduleId &&
                moduleId == that.moduleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, moduleId);
    }
}
