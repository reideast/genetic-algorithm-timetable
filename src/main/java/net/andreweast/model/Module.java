package net.andreweast.model;

import javax.persistence.*;
import java.util.List;
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

    @OneToMany(mappedBy = "module")
    private List<CourseModule> courseModules;

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

    public List<CourseModule> getCourseModules() {
        return courseModules;
    }

    public void setCourseModules(List<CourseModule> courseModules) {
        this.courseModules = courseModules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return moduleId.equals(module.moduleId) &&
                name.equals(module.name) &&
                Objects.equals(lecturer, module.lecturer) &&
                Objects.equals(courseModules, module.courseModules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, name, lecturer, courseModules);
    }
}
