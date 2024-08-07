package com.fourback.bemajor.service;


import com.fourback.bemajor.domain.*;
import com.fourback.bemajor.dto.PostDto;
import com.fourback.bemajor.dto.PostListDto;

import com.fourback.bemajor.dto.PostUpdateDto;
import com.fourback.bemajor.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PostImageRepository imageRepository;
    private final ImageService imageService;
    private final FavoritePostRepository favoritePostRepository;
    private final FavoriteCommentRepository favoriteCommentRepository;
    private final CommentRepository commentRepository;
    private final ImageFileService imageFileService;

    @Transactional
    public Long create(PostDto postDto, String oauth2Id, MultipartFile[] imageFiles) throws IOException {
        Post post = new Post();
        Optional<Board> optionalBoard = boardRepository.findById((postDto.getBoardId()));
        Board board = optionalBoard.orElse(new Board());
        User user = userRepository.findByOauth2Id(oauth2Id).orElse(null);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setBoard(board);
        post.setUser(user);
        postRepository.save(post);
        if (imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                PostImage image = new PostImage();
                image.setPost(post);
                imageService.saveImage(image, imageFile);
            }
        }
        return post.getId();
    }

    public List<PostListDto> posts(PageRequest pageRequest, Long boardId, String oauth2Id) {

        Page<Post> pagePost = postRepository.findAllWithPost(boardId, pageRequest);
        Optional<User> byOauth2Id = userRepository.findByOauth2Id(oauth2Id);
        User user = byOauth2Id.get();
        List<PostListDto> postListDtos = pagePost.stream()
                .map(p -> {
                    boolean userCheck = false;
                    if (p.getUser().getUserId().equals(user.getUserId())) {
                        userCheck = true;
                    }
                    List<PostImage> imageList = imageRepository.findByPostId(p.getId());

                    String postDate;
                    LocalDateTime currentTime = LocalDateTime.now();
                    Duration duration = Duration.between(p.getCreatedDate(), currentTime);
                    long minutes = duration.toMinutes();
                    if (minutes < 1) {
                        postDate = "방금 전";
                    } else if (minutes < 60) {
                        postDate = minutes + "분 전";
                    } else {
                        long hours = duration.toHours();
                        if (hours < 24) {
                            postDate = hours + "시간 전";
                        } else {
                            long days = duration.toDays();
                            postDate = days + "일 전";
                        }
                    }
                    Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(user, p);
                    boolean postGood = false;
                    if (optionalFavoritePost.isPresent()) {
                        postGood = true;
                    }
                    return new PostListDto(p, postDate, imageList, postGood, userCheck);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    public List<PostListDto> posts2(PageRequest pageRequest, Long boardId, String oauth2Id) {
        Optional<User> byOauth2Id = userRepository.findByOauth2Id(oauth2Id);
        User user = byOauth2Id.get();
        Page<Post> pagePost = null;

        if (boardId == 1) {
            pagePost = postRepository.findAllMyPost(user.getUserId(), pageRequest);
        } else if (boardId == 2) {
            pagePost = commentRepository.findCommentPosts(user.getUserId(), pageRequest);
        } else if (boardId == 3) {
            pagePost = favoritePostRepository.findFavoritePosts(user.getUserId(), pageRequest);
        } else if (boardId == 4) {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.minusWeeks(1);
            pagePost = postRepository.findPopularPosts(startDate, endDate, pageRequest);
        }


        List<PostListDto> postListDtos = pagePost.stream()
                .map(p -> {
                    boolean userCheck = false;
                    if (p.getUser().getUserId().equals(user.getUserId())) {
                        userCheck = true;
                    }
                    List<PostImage> imageList = imageRepository.findByPostId(p.getId());

                    String postDate;
                    LocalDateTime currentTime = LocalDateTime.now();
                    Duration duration = Duration.between(p.getCreatedDate(), currentTime);
                    long minutes = duration.toMinutes();
                    if (minutes < 1) {
                        postDate = "방금 전";
                    } else if (minutes < 60) {
                        postDate = minutes + "분 전";
                    } else {
                        long hours = duration.toHours();
                        if (hours < 24) {
                            postDate = hours + "시간 전";
                        } else {
                            long days = duration.toDays();
                            postDate = days + "일 전";
                        }
                    }
                    Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(user, p);
                    boolean postGood = false;
                    if (optionalFavoritePost.isPresent()) {
                        postGood = true;
                    }
                    return new PostListDto(p, postDate, imageList, postGood, userCheck);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    public List<PostListDto> postSearch(PageRequest pageRequest, String keyword, String oauth2Id) {
        Page<Post> posts = postRepository.findAllSearchPost(keyword, pageRequest);
        Optional<User> byOauth2Id = userRepository.findByOauth2Id(oauth2Id);
        User user = byOauth2Id.get();
        List<PostListDto> postListDtos = posts.stream()
                .map(p -> {
                    boolean userCheck = false;
                    if (p.getUser().getUserId().equals(user.getUserId())) {
                        userCheck = true;
                    }
                    List<PostImage> imageList = imageRepository.findByPostId(p.getId());

                    String postDate;
                    LocalDateTime currentTime = LocalDateTime.now();
                    Duration duration = Duration.between(p.getCreatedDate(), currentTime);
                    long minutes = duration.toMinutes();
                    if (minutes < 1) {
                        postDate = "방금 전";
                    } else if (minutes < 60) {
                        postDate = minutes + "분 전";
                    } else {
                        long hours = duration.toHours();
                        if (hours < 24) {
                            postDate = hours + "시간 전";
                        } else {
                            long days = duration.toDays();
                            postDate = days + "일 전";
                        }
                    }
                    Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(user, p);
                    boolean postGood = false;
                    if (optionalFavoritePost.isPresent()) {
                        postGood = true;
                    }
                    return new PostListDto(p, postDate, imageList, postGood, userCheck);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    @Transactional
    public ResponseEntity<String> update(Long postId, String title, String content, MultipartFile[] images) throws IOException {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();
        post.setTitle(title);
        post.setContent(content);
        if (images != null) {
            for (MultipartFile imageFile : images) {
                PostImage image = new PostImage();
                image.setPost(post);
                imageService.saveImage(image, imageFile);
            }


        }
        return ResponseEntity.ok("post update");
    }


    public PostUpdateDto updatePostGet(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();
        String postDate;
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(post.getUpdatedDate(), currentTime);
        long minutes = duration.toMinutes();
        if (minutes < 1) {
            postDate = "방금 전";
        } else if (minutes < 60) {
            postDate = minutes + "분 전";
        } else {
            long hours = duration.toHours();
            if (hours < 24) {
                postDate = hours + "시간 전";
            } else {
                long days = duration.toDays();
                postDate = days + "일 전";
            }
        }
        List<PostImage> imageList = imageRepository.findByPostId(post.getId());
        PostUpdateDto postUpdateDto = new PostUpdateDto(post, postDate, imageList);
        return postUpdateDto;
    }

    @Transactional
    public void delete(Long postId) throws IOException {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();

        favoritePostRepository.deleteByPostId(postId);
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            Optional<FavoriteComment> byCommentId = favoriteCommentRepository.findByCommentId(comment.getId());
            if (byCommentId.isPresent()) {
                FavoriteComment favoriteComment = byCommentId.get();
                favoriteCommentRepository.delete(favoriteComment);
            }

            List<Comment> byParentId = commentRepository.findByParentId(comment.getId());
            for (Comment comment1 : byParentId) {
                Optional<FavoriteComment> byCommentId1 = favoriteCommentRepository.findByCommentId(comment1.getId());
                if (byCommentId1.isPresent()) {
                    FavoriteComment favoriteComment1 = byCommentId1.get();
                    favoriteCommentRepository.delete(favoriteComment1);
                }
                commentRepository.delete(comment1);
            }

            commentRepository.delete(comment);
        }

        List<PostImage> images = imageRepository.findByPostId(postId);
        for (PostImage image : images) {
            imageFileService.deleteImageFile(image.getFilePath());
            imageRepository.delete(image);
        }
        postRepository.delete(post);
    }

    @Transactional
    public ResponseEntity<String> viewCountUp(Long postId) {
        Post post = postRepository.findById(postId).get();
        post.setViewCount(post.getViewCount() + 1);
        return ResponseEntity.ok("ok");

    }
}
