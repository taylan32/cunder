package com.example.cunder.service;

import com.example.cunder.dto.match.MatchDto;
import com.example.cunder.exception.MathRequestLimitExceedException;
import com.example.cunder.model.Match;
import com.example.cunder.model.User;
import com.example.cunder.model.enums.Gender;
import com.example.cunder.model.enums.MembershipType;
import com.example.cunder.repository.MatchRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserService userService;

    public MatchService(MatchRepository matchRepository, UserService userService) {
        this.matchRepository = matchRepository;
        this.userService = userService;
    }


    public List<MatchDto> match() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.findByUsername(username);
        int suggestionCount = getSuggestionCount(user.getMembershipType());
        int remainingSuggestionsCount = checkIfDailyLimitExceed(user.getId(), user.getMembershipType(), suggestionCount);
        Gender genderToSuggest = getGenderToSuggest(user.getGender());
        Set<User> matchedUsers = userService.getSuggestion(genderToSuggest.name(), remainingSuggestionsCount);
        List<Match> matches = matchedUsers.stream().map(matchedUser -> new Match(user, matchedUser, new Date())).collect(Collectors.toList());
        List<Match> createdMatches = matchRepository.saveAll(matches);

        return createdMatches.stream().map(MatchDto::convert).collect(Collectors.toList());
    }

    private int checkIfDailyLimitExceed(String userId, MembershipType membershipType, int suggestionCount) {
        int count = matchRepository.countByUser_Id(userId);
        // TODO move count to the env file later
        if ((membershipType.equals(MembershipType.STANDARD) && count >= 5) || (membershipType.equals(MembershipType.PREMIUM) && count >= 100)) {
            throw new MathRequestLimitExceedException("Match limit exceeded. Your limit is " + suggestionCount);
        }
        return suggestionCount - count;
    }

    private Gender getGenderToSuggest(Gender gender) {
        if (gender.equals(Gender.FEMALE)) {
            return Gender.MALE;
        }
        return Gender.FEMALE;
    }

    private int getSuggestionCount(MembershipType membershipType) {
        if (membershipType.equals(MembershipType.PREMIUM)) {
            return 100;
        }
        if (membershipType.equals(MembershipType.ADMIN)) {
            return Integer.MAX_VALUE;
        }
        return 5;
    }


}
