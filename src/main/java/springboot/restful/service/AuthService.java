package springboot.restful.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import springboot.restful.entity.User;
import springboot.restful.model.TokenResponse;
import springboot.restful.repository.UserRepository;
import springboot.restful.request.LoginUserRequest;
import springboot.restful.security.BCrypt;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValdiationService valdiationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        valdiationService.validate(request);

        User user = userRepository.findById(request.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password is wrong!"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .token(user.getToken())
                .expiredAt(user.getTokenExpiredAt())
                .build();

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password is wrong!");
        }
    }

    private long next30Days() {
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }
}
