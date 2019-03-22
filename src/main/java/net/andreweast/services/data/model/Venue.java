package net.andreweast.services.data.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "venues", schema = "public", catalog = "ga_dev")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venue_generator") // DEBUG: Added manually. SEQUENCE for Postgres
    @SequenceGenerator(name="venue_generator", sequenceName = "venue_id_sequence", allocationSize = 1)
    @Column(name = "venue_id", updatable = false, nullable = false)
    private Long venueId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "is_lab", nullable = true)
    private Boolean isLab = false;

    @Basic
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @OneToMany(mappedBy = "venue")
    private List<ScheduledModule> scheduledModules;

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getLab() {
        return isLab;
    }

    public void setLab(Boolean lab) {
        isLab = lab;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<ScheduledModule> getScheduledModules() {
        return scheduledModules;
    }

    public void setScheduledModules(List<ScheduledModule> scheduledModules) {
        this.scheduledModules = scheduledModules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return venueId.equals(venue.venueId) &&
                name.equals(venue.name) &&
                Objects.equals(isLab, venue.isLab) &&
                capacity.equals(venue.capacity) &&
                Objects.equals(building, venue.building) &&
                Objects.equals(scheduledModules, venue.scheduledModules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueId, name, isLab, capacity, building, scheduledModules);
    }
}
