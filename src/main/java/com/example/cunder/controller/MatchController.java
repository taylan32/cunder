package com.example.cunder.controller;

import com.example.cunder.dto.match.MatchDto;
import com.example.cunder.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/match")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }


    @GetMapping
    public ResponseEntity<List<MatchDto>> match() {
        return ResponseEntity.ok(matchService.match());
    }




}
