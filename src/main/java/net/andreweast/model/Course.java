package net.andreweast.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "courses", schema = "public", catalog = "ga_dev")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_generator")
    @SequenceGenerator(name = "course_generator", sequenceName = "course_id_sequence", allocationSize = 1)
    @Column(name = "course_id", updatable = false, nullable = false)
    private Long courseId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId.equals(course.courseId) &&
                name.equals(course.name) &&
                Objects.equals(department, course.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, name, department);
    }
}
