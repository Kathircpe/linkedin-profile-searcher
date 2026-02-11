package com.finder.core.repository;

import com.finder.core.model.LinkedInProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<LinkedInProfile,Long> {

}
