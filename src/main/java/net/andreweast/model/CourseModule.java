package net.andreweast.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "course_module", schema = "public", catalog = "ga_dev")
public class CourseModule {
    @EmbeddedId
    private CourseModulePK id;

    @ManyToOne
    @MapsId("course_id")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @MapsId("module_id")
    @JoinColumn(name = "module_id")
    private Module module;

    /**
     * A course code, assigned on a per-course basis (e.g. Cryptography is CS402 for IT and MA492 for Maths)
     * This allows cross listed courses to "belong" to many courses/departments, but only need to be scheduled once as a Module
     */
    @Basic
    @Column(name = "code", nullable = true, length = -1)
    private String code;

    public CourseModulePK getId() {
        return id;
    }

    public void setId(CourseModulePK id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseModule that = (CourseModule) o;
        return id.equals(that.id) &&
                course.equals(that.course) &&
                module.equals(that.module) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, module, code);
    }
}
