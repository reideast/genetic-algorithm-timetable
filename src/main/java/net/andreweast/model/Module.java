package net.andreweast.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "modules", schema = "public", catalog = "ga_dev")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "module_generator")
    @SequenceGenerator(name = "module_generator", sequenceName = "module_id_sequence", allocationSize = 1)
    @Column(name = "module_id", updatable = false, nullable = false)
    private Long moduleId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    // TODO
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module that = (Module) o;
        return moduleId == that.moduleId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, name);
    }

}
