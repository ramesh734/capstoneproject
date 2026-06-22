package com.wip.asset_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String employeeName;

    @Column(unique = true)
    private String email;

    private String department;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;
}