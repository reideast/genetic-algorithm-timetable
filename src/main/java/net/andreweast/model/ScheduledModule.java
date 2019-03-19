package net.andreweast.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "scheduled_modules", schema = "public", catalog = "ga_dev")
public class ScheduledModule {
    @EmbeddedId
    private ScheduledModulePK id;

    @ManyToOne
    @MapsId("schedule_id")
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @MapsId("module_id")
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public ScheduledModulePK getId() {
        return id;
    }

    public void setId(ScheduledModulePK id) {
        this.id = id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledModule that = (ScheduledModule) o;
        return id.equals(that.id) &&
                schedule.equals(that.schedule) &&
                module.equals(that.module) &&
                timeslot.equals(that.timeslot) &&
                venue.equals(that.venue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schedule, module, timeslot, venue);
    }
}
