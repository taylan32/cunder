package com.example.cunder.dto.match;

import com.example.cunder.dto.user.UserDto;
import com.example.cunder.model.Match;


public record MatchDto(
        UserDto matchedUser
) {

    public static MatchDto convert(Match from) {
        return new MatchDto(
                UserDto.convert(from.getMatcherUser())
        );

    }

}
