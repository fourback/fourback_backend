package com.fourback.bemajor.domain.user.controller;

import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.service.UserService;
import com.fourback.bemajor.global.common.response.Response;
import com.fourback.bemajor.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        HttpHeaders tokens = userService.save(userLoginRequestDto);
        return Response.onSuccess(tokens);

    }

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserResponseDto userResponseDto = userService.get(customUserDetails.getUserId());
        return Response.onSuccess(userResponseDto);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.update(userUpdateRequestDto, customUserDetails.getUserId());
        return Response.onSuccess();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails)
            throws IOException {
        userService.delete(customUserDetails.getUserId());
        return Response.onSuccess();
    }

    @PostMapping("/image")
    public ResponseEntity<?> updateImage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        String filename = userService.saveImage(customUserDetails.getUserId(), file);
        return Response.onSuccess(filename);
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal CustomUserDetails customUserDetails)
            throws IOException {
        userService.deleteImage(customUserDetails.getUserId());
        return Response.onSuccess();
    }
}

