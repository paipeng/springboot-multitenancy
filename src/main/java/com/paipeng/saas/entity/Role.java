package com.paipeng.saas.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Column(name = "role")
    private String role;

    /**
     * Defining the Many-to-Many relation of users and roles. A Role can belong
     * to many Users and many Users can belong to a Role.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    // Getters and setters


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
