package net.andreweast.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CourseModulePK implements Serializable {
    private int courseId;
    private int moduleId;

    @Id
    @Column(name = "course_id", nullable = false)
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Id
    @Column(name = "module_id", nullable = false)
    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseModulePK that = (CourseModulePK) o;
        return courseId == that.courseId &&
                moduleId == that.moduleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, moduleId);
    }
}
