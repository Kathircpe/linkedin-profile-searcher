package com.finder.core.repository;

import com.finder.core.model.LinkedInProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<LinkedInProfile,Long> {

}
