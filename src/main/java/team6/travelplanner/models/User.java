package team6.travelplanner.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "userprofile")
public class User {
    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false, unique = true)
    String username;

    String password;

    String email;
    boolean isAccountNonExpired;

    boolean isAccountNonLocked;

    boolean isCredentialsNonExpired;

    boolean isEnabled;
}
