package com.fourback.bemajor.domain.image.repository;

import com.fourback.bemajor.domain.image.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Long id);
}