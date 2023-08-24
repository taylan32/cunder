package com.example.cunder.dto.user;

import com.example.cunder.model.enums.MembershipType;
import jakarta.validation.constraints.NotNull;

public record ChangeMembershipTypeRequest(
        @NotNull
                // TODO: add custom annotation to check if it is a valid membership type
        MembershipType membershipType
) {
}
