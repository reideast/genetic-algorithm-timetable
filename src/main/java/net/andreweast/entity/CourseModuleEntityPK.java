package net.andreweast.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CourseModuleEntityPK implements Serializable {
    private int courseId;
    private int moduleId;

    @Column(name = "course_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Column(name = "module_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
        CourseModuleEntityPK that = (CourseModuleEntityPK) o;
        return courseId == that.courseId &&
                moduleId == that.moduleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, moduleId);
    }
}
