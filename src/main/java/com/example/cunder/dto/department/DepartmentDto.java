package com.example.cunder.dto.department;

import com.example.cunder.model.Department;

public record DepartmentDto(
        String id,
        String departmentName,
        String code
) {

    public static DepartmentDto convert(Department from) {
        return new DepartmentDto(
                from.getId(),
                from.getDepartmentName(),
                from.getCode()
        );
    }

}
