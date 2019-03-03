package net.andreweast.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "scheduled_timeslots", schema = "public", catalog = "ga_dev")
public class ScheduledTimeslotsEntity {
    private ModulesEntity modulesByModuleId;

    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "module_id", nullable = false)
    public ModulesEntity getModulesByModuleId() {
        return modulesByModuleId;
    }

    public void setModulesByModuleId(ModulesEntity modulesByModuleId) {
        this.modulesByModuleId = modulesByModuleId;
    }
}
