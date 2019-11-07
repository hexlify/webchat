package com.webchat.api.privateCabinet;

import com.webchat.domain.user.User;
import com.webchat.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "u")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Hello, world!";
    }
}
