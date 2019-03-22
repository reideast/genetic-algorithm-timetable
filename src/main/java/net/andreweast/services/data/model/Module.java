package net.andreweast.services.data.model;

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

    @Basic
    @Column(name = "is_lab", nullable = true)
    private Boolean isLab = false;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @OneToMany(mappedBy = "module")
    private List<CourseModule> courseModules;

    @OneToMany(mappedBy = "module")
    private List<ScheduledModule> scheduledModules;

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

    public Boolean getLab() {
        return isLab;
    }

    public void setLab(Boolean lab) {
        isLab = lab;
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
        Module module = (Module) o;
        return moduleId.equals(module.moduleId) &&
                name.equals(module.name) &&
                Objects.equals(isLab, module.isLab) &&
                Objects.equals(lecturer, module.lecturer) &&
                Objects.equals(courseModules, module.courseModules) &&
                Objects.equals(scheduledModules, module.scheduledModules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, name, isLab, lecturer, courseModules, scheduledModules);
    }
}
