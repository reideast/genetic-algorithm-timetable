package net.andreweast.model;

import javax.persistence.*;
import java.util.List;
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

    @Basic
    @Column(name= "numenrolled", nullable = false)
    private Integer numEnrolled;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "course")
    private List<CourseModule> courseModules;

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

    public Integer getNumEnrolled() {
        return numEnrolled;
    }

    public void setNumEnrolled(Integer numEnrolled) {
        this.numEnrolled = numEnrolled;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
        Course course = (Course) o;
        return courseId.equals(course.courseId) &&
                name.equals(course.name) &&
                numEnrolled.equals(course.numEnrolled) &&
                Objects.equals(department, course.department) &&
                Objects.equals(courseModules, course.courseModules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, name, numEnrolled, department, courseModules);
    }
}
