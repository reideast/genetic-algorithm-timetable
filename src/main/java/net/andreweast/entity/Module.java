package net.andreweast.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "modules", schema = "public", catalog = "ga_dev")
public class Module {
    private int moduleId;
    private String name;
    private Lecturer lecturerByLecturerId;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "module_generator")
    @SequenceGenerator(name="module_generator", sequenceName = "module_id_sequence", allocationSize = 1)
    @Column(name = "module_id", updatable = false, nullable = false)
    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    @ManyToOne
    @JoinColumn(name = "lecturer_id", referencedColumnName = "lecturer_id")
    public Lecturer getLecturerByLecturerId() {
        return lecturerByLecturerId;
    }

    public void setLecturerByLecturerId(Lecturer lecturerByLecturerId) {
        this.lecturerByLecturerId = lecturerByLecturerId;
    }
}
