package com.example.cunder.model;

import com.example.cunder.model.enums.Gender;
import com.example.cunder.model.enums.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
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

    @Column(name = "birth_of_date")
    private LocalDate birthOfDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "cover_image")
    private String coverImage;

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
                LocalDate birthOfDate,
                Gender gender,
                String profileImage,
                String coverImage,
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
        this.birthOfDate = birthOfDate;
        this.gender = gender;
        this.profileImage = profileImage;
        this.coverImage = coverImage;
        this.isDeleted = isDeleted;
        this.isVerified = isVerified;
        this.isBanned = isBanned;
        this.membershipType = membershipType;
        this.roles = roles;
    }


}
