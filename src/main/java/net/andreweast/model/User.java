package net.andreweast.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public", catalog = "ga_dev")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_id_sequence", allocationSize = 1)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Basic
    @Column(name = "username", nullable = false, length = -1)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Basic
    @Column(name = "password_salt", nullable = false, length = 256)
    private String passwordSalt;

    @Basic
    @Column(name = "display_name", nullable = false, length = -1)
    private String displayName;

    @Basic
    @Column(name = "is_facilities", nullable = true)
    private Boolean isFacilities = false; // Default value

    @Basic
    @Column(name = "is_admin", nullable = true)
    private Boolean isAdmin = false; // Default value

    @Basic
    @Column(name = "email", nullable = true, length = -1)
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "creator")
    private List<Schedule> schedules;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getFacilities() {
        return isFacilities;
    }

    public void setFacilities(Boolean facilities) {
        isFacilities = facilities;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId) &&
                username.equals(user.username) &&
                password.equals(user.password) &&
                passwordSalt.equals(user.passwordSalt) &&
                displayName.equals(user.displayName) &&
                Objects.equals(isFacilities, user.isFacilities) &&
                Objects.equals(isAdmin, user.isAdmin) &&
                Objects.equals(email, user.email) &&
                Objects.equals(department, user.department) &&
                Objects.equals(schedules, user.schedules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, passwordSalt, displayName, isFacilities, isAdmin, email, department, schedules);
    }
}
