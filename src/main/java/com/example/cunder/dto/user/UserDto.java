package com.example.cunder.dto.user;

import com.example.cunder.dto.department.DepartmentDto;
import com.example.cunder.model.User;
import com.example.cunder.model.enums.MembershipType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record UserDto(
        String id,
        String email,
        String firstName,
        String lastName,
        DepartmentDto department,
        LocalDate birthOfDate,
        String profileImage,
        String coverImage,
        MembershipType membershipType

) {

    public static UserDto convert(User from) {
        return new UserDto(
                from.getId(),
                from.getEmail(),
                from.getFirstName(),
                from.getLastName(),
                DepartmentDto.convert(from.getDepartment()),
                from.getBirthOfDate(),
                from.getProfileImage(),
                from.getCoverImage(),
                from.getMembershipType()
        );
    }

}
