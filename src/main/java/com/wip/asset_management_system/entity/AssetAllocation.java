package com.wip.asset_management_system.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "asset_allocations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationId;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate allocatedDate;

    private LocalDate returnDate;

    private String status;

    private String remarks;
}
