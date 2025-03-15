package com.andres.curso.springboot.app.springbootcrud.entities;

import com.andres.curso.springboot.app.springbootcrud.validation.ExistsByUsername;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExistsByUsername // clase validation creadi por unomismo
    @Column(unique = true)
    @NotBlank // no puede ser vacio
    @Size(min = 4, max = 12)
    private String username;

    @NotBlank // no puede ser vacio
    // @JsonIgnore simplemente lo ignora
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // solo cuando se escribe (metodo post), no cuando se manda (get)
    private String password;

    @JsonIgnoreProperties({"userList", "handler", "hibernateLazyInitializer"})// json editado
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roleList;

    @Transient // dice que solo es atributo de la clase no de la bd coño
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin; // solo en la clase no en la bd

    private boolean enable; // como null automaticamente, pa saber si esta activo

    public User() {
        this.roleList = new ArrayList<>();
    }

    @PrePersist
    public void prePersist () {
        enable = true;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
