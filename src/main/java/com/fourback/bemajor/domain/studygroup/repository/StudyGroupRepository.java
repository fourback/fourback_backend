package com.fourback.bemajor.domain.studygroup.repository;

import com.fourback.bemajor.domain.studygroup.entity.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyGroupRepository extends JpaRepository<StudyGroup,Long> {
    Page<StudyGroup> findAll(Pageable pageable);
    Page<StudyGroup> findAllByCategory(String category,Pageable pageable);
}
