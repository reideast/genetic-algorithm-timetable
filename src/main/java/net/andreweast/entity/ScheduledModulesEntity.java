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

    private int scheduleId;
    private int moduleId;
    private int timeslotId;
    private int venueId;
//    private ModulesEntity modulesByModuleId;

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

    @Basic
    @Column(name = "timeslot_id", nullable = false)
    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Basic
    @Column(name = "venue_id", nullable = false)
    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledModulesEntity that = (ScheduledModulesEntity) o;
        return scheduleId == that.scheduleId &&
                moduleId == that.moduleId &&
                timeslotId == that.timeslotId &&
                venueId == that.venueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, moduleId, timeslotId, venueId);
    }


    //    @ManyToOne
//    @JoinColumn(name = "module_id", referencedColumnName = "module_id", nullable = false)
//    public ModulesEntity getModulesByModuleId() {
//        return modulesByModuleId;
//    }
//
//    public void setModulesByModuleId(ModulesEntity modulesByModuleId) {
//        this.modulesByModuleId = modulesByModuleId;
//    }
}
