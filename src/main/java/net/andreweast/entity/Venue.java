package net.andreweast.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "venues", schema = "public", catalog = "ga_dev")
public class Venue {
    private int venueId;
    private String name;
    private Boolean isLab;
    private int capacity;
    private Building buildingByBuildingId;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venue_generator") // DEBUG: Added manually. SEQUENCE for Postgres
    @SequenceGenerator(name="venue_generator", sequenceName = "venue_id_sequence", allocationSize = 1)
    @Column(name = "venue_id", updatable = false, nullable = false)
    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "is_lab", nullable = true)
    public Boolean getLab() {
        return isLab;
    }

    public void setLab(Boolean lab) {
        isLab = lab;
    }

    @Basic
    @Column(name = "capacity", nullable = false)
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue that = (Venue) o;
        return venueId == that.venueId &&
                capacity == that.capacity &&
                Objects.equals(name, that.name) &&
                Objects.equals(isLab, that.isLab);
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueId, name, isLab, capacity);
    }

    @ManyToOne
    @JoinColumn(name = "building_id", referencedColumnName = "building_id")
    public Building getBuildingByBuildingId() {
        return buildingByBuildingId;
    }

    public void setBuildingByBuildingId(Building buildingByBuildingId) {
        this.buildingByBuildingId = buildingByBuildingId;
    }
}
