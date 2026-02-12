package com.finder.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class LinkedInProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String fullName;
    @Column(name = "\"current_role\"")
    private String currentRole;
    private String university;
    private String location;
    private String linkedInHeadLine;
    private Integer passoutYear;
    private String profileUrl;

}
