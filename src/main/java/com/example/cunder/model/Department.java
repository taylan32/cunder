package com.example.cunder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<User> users;


    public Department(String departmentName, String code) {
        this.departmentName = departmentName;
        this.code = code;
    }
}
