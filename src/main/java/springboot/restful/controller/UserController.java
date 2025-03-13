package springboot.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import springboot.restful.model.UserResponse;
import springboot.restful.model.WebResponse;
import springboot.restful.request.RegisterUserRequest;
import springboot.restful.service.UserService;
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
}
