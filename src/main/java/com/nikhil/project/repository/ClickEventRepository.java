package com.nikhil.project.repository;

import com.nikhil.project.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    List<ClickEvent> findByShortcodeOrderByTimestampAsc(String shortcode);
}

