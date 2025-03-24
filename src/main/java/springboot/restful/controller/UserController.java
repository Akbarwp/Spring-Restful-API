package springboot.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.entity.User;
import springboot.restful.model.UserResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.RegisterUserRequest;
import springboot.restful.request.UpdateUserRequest;
import springboot.restful.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
        path = "/auth/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        UserResponse userResponse = userService.getByEmail(request.getEmail());

        return WebResponse.<UserResponse> builder()
            .messages("Register success")
            .data(userResponse)
            .build();
    }

    @GetMapping(
        path = "/users/current",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user) {
        UserResponse userResponse = userService.get(user);

        return WebResponse.<UserResponse> builder()
            .data(userResponse)
            .build();
    }

    @PatchMapping(
        path = "/users/current",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.update(user, request);

        return WebResponse.<UserResponse> builder()
            .messages("Update user success")
            .data(userResponse)
            .build();
    }
}
