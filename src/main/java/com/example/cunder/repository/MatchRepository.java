package com.example.cunder.repository;

import com.example.cunder.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, String> {

    int countByUser_Id(String userId);

}
