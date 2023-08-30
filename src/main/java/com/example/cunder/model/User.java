package com.example.cunder.model;

import com.example.cunder.model.enums.Gender;
import com.example.cunder.model.enums.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "cover_image")
    private String coverImage;

    private String bio;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "is_banned")
    private boolean isBanned;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_type")
    private MembershipType membershipType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(String email,
                String firstName,
                String lastName,
                Department department,
                String username,
                String password,
                LocalDate birthDate,
                Gender gender,
                String profileImage,
                String coverImage,
                String bio,
                boolean isDeleted,
                boolean isVerified,
                boolean isBanned,
                MembershipType membershipType,
                Set<Role> roles) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.profileImage = profileImage;
        this.coverImage = coverImage;
        this.bio = bio;
        this.isDeleted = isDeleted;
        this.isVerified = isVerified;
        this.isBanned = isBanned;
        this.membershipType = membershipType;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && email.equals(user.email) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username);
    }
}
