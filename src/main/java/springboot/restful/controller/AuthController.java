package springboot.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.entity.User;
import springboot.restful.model.TokenResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.LoginUserRequest;
import springboot.restful.service.AuthService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
        path = "/auth/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);

        return WebResponse.<TokenResponse>builder()
            .messages("Login success")
            .data(tokenResponse)
            .build();
    }

    @DeleteMapping(
        path = "/auth/logout",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> logout(User user) {
        authService.logout(user);

        return WebResponse.<String>builder()
            .messages("Logout success")
            .build();
    }
}
