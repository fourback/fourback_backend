package com.fourback.bemajor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studyName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer teamSize;
    private String studyLocation;
    private String category;
    private String studyCycle;
    private String studyRule;
    private String ownerOauth2Id;

    public StudyGroup(String studyName,LocalDateTime startDate, LocalDateTime endDate, Integer teamSize, String studyLocation, String category, String studyCycle, String studyRule, String ownerOauth2Id) {
        this.studyName = studyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamSize = teamSize;
        this.studyLocation = studyLocation;
        this.category = category;
        this.studyCycle = studyCycle;
        this.studyRule = studyRule;
        this.ownerOauth2Id = ownerOauth2Id;
    }
    public void updateStudyGroup(String studyName,LocalDateTime startDate, LocalDateTime endDate, Integer teamSize, String studyLocation, String category, String studyCycle, String studyRule){
        this.studyName = studyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamSize = teamSize;
        this.studyLocation = studyLocation;
        this.category = category;
        this.studyCycle = studyCycle;
        this.studyRule = studyRule;
    }
}
