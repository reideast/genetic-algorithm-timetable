package net.andreweast.services.data.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departments", schema = "public", catalog = "ga_dev")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_generator")
    @SequenceGenerator(name="department_generator", sequenceName = "department_id_sequence", allocationSize = 1)
    @Column(name = "department_id", updatable = false, nullable = false)
    private Long departmentId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<Course> courses;

    @OneToMany(mappedBy = "department")
    private List<Lecturer> lecturers;

    @OneToMany(mappedBy = "department")
    private List<User> users;

    // This expresses a Many-To-Many relationship, but the case where the JoinTable has data fields that are important
    // See: https://www.baeldung.com/jpa-many-to-many
    // For a basic @ManyToMany relation, where the join table has no extra data in it, see: https://www.baeldung.com/jpa-many-to-many
    @OneToMany(mappedBy = "department")
    private List<DepartmentBuilding> departmentBuildings;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(List<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<DepartmentBuilding> getDepartmentBuildings() {
        return departmentBuildings;
    }

    public void setDepartmentBuildings(List<DepartmentBuilding> departmentBuildings) {
        this.departmentBuildings = departmentBuildings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return departmentId.equals(that.departmentId) &&
                name.equals(that.name) &&
                Objects.equals(courses, that.courses) &&
                Objects.equals(lecturers, that.lecturers) &&
                Objects.equals(users, that.users) &&
                Objects.equals(departmentBuildings, that.departmentBuildings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, name, courses, lecturers, users, departmentBuildings);
    }
}
