package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.LoginUserDto;
import com.fourback.bemajor.dto.TokenDto;
import com.fourback.bemajor.dto.UserDto;
import com.fourback.bemajor.exception.NotFoundElementException;
import com.fourback.bemajor.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    @Transactional
    public User findByOauth2Id(String oauth2Id){
        Optional<User> ou = userRepository.findByOauth2Id(oauth2Id);
        if(ou.isEmpty()){
            throw new NotFoundElementException(1, "That is not in DB", HttpStatus.NOT_FOUND);
        }
        return ou.get();
    }

    @Transactional
    public TokenDto save(LoginUserDto loginUserDto){
        String registrationId = loginUserDto.getRegistrationId();
        String oauth2Id = registrationId + loginUserDto.getUserId();
        Optional<User> ou = userRepository.findByOauth2Id(oauth2Id);
        User user;
        if (ou.isEmpty()) {
            user = User.builder()
                    .role("ROLE_USER")
                    .oauth2Id(oauth2Id)
                    .build();
            userRepository.save(user);
        } else {
            user = ou.get();
        }
        return authService.newToken(user.getOauth2Id(), user.getRole());
    }

    @Transactional
    public UserDto get(String oauth2Id){
        User user = findByOauth2Id(oauth2Id);
        return user.toUserDto();
    }

    @Transactional
    public void update(UserDto userDto, String oauth2Id){
        User user = findByOauth2Id(oauth2Id);
        user.setUserDto(userDto);
        userRepository.save(user);
    }
}
