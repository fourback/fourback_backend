package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.aws.UserCloudImage;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 유저 클라우드 이미지 리포지토리
 */
public interface UserCloudImageRepository extends JpaRepository<UserCloudImage, Long> {
}
