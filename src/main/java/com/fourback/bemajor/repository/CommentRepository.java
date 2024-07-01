package com.fourback.bemajor.repository;

import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.Image;
import com.fourback.bemajor.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository{

    List<Comment> findByPostId(Long id);
    void deleteByPostId(Long postId);
    List<Comment> findByParentId(Long parentId);

    @Query("SELECT c.post FROM Comment c WHERE c.user.id = :userId")
    Page<Post> findCommentPosts(@Param("userId") Long userId, PageRequest pageRequest);

}
