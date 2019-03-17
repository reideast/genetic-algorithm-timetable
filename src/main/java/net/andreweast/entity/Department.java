package net.andreweast.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departments", schema = "public", catalog = "ga_dev")
public class Department {
    private int departmentId;
    private String name;

    private List<Course> courses = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_generator")
    @SequenceGenerator(name="department_generator", sequenceName = "department_id_sequence", allocationSize = 1)
    @Column(name = "department_id", updatable = false, nullable = false)
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // @JoinColumn(name = "department_id") // Joins are fine, but generate multiple SQL queries vs. a @OneToMany(mappedBy) style. See: https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/

    @OneToMany(mappedBy = "department") // DEBUG: what is "department"? It's not a table, that would be departmentS
    public List<Course> getCourses() {
        return courses;
    }
    // This style of add/remove for the One side of a OneToMany relation is detailed here: https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/

    public void addCourse(Course course) {
        courses.add(course);
        course.setDepartment(this);
    }
    public void removeCourse(Course course) {
        courses.remove(course);
        course.setDepartment(null);
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return departmentId == that.departmentId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, name);
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
