package com.nikhil.project.repository;

import com.nikhil.project.entity.UrlEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntry, String> {
    Optional<UrlEntry> findByShortcode(String shortcode);
}

