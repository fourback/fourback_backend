package com.fourback.bemajor.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.Post;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Data
public class CommentResult {
    private Long id;
    private String userName;
    private String content;
    private int goodCount;
    private LocalDateTime commentDate;
    private long postId;
    private long parentId;
    private String dateDiff;
    private boolean isFavorite;
    private boolean userCheck;
    private int status;

    private GetCommentListResponse reply;

    public static CommentResult fromComment(Comment comment) {
        return CommentResult.builder()
                .id(comment.getId())
                .userName(comment.getUser().getUserName())
                .content(comment.getContent())
                .goodCount(comment.getGoodCount())
                .commentDate(comment.getCreatedDate())
                .build();
    }
}