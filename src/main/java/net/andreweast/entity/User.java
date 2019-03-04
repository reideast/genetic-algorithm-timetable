package net.andreweast.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public", catalog = "ga_dev")
public class User {
    private int userId;
    private String username;
    private String password;
    private String passwordSalt;
    private String displayName;
    private Boolean isFacilities;
    private Boolean isAdmin;
    private String email;
    private Department departmentByDepartmentId;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name="user_generator", sequenceName = "user_id_sequence", allocationSize = 1)
    @Column(name = "user_id", updatable = false, nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "username", nullable = false, length = -1)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 256)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "password_salt", nullable = false, length = 256)
    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    @Basic
    @Column(name = "display_name", nullable = false, length = -1)
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Basic
    @Column(name = "is_facilities", nullable = true)
    public Boolean getFacilities() {
        return isFacilities;
    }

    public void setFacilities(Boolean facilities) {
        isFacilities = facilities;
    }

    @Basic
    @Column(name = "is_admin", nullable = true)
    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Basic
    @Column(name = "email", nullable = true, length = -1)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return userId == that.userId &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(passwordSalt, that.passwordSalt) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(isFacilities, that.isFacilities) &&
                Objects.equals(isAdmin, that.isAdmin) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, passwordSalt, displayName, isFacilities, isAdmin, email);
    }

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    public Department getDepartmentByDepartmentId() {
        return departmentByDepartmentId;
    }

    public void setDepartmentByDepartmentId(Department departmentByDepartmentId) {
        this.departmentByDepartmentId = departmentByDepartmentId;
    }
}
