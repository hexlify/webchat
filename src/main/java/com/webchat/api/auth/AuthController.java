package com.webchat.api.auth;

import com.webchat.domain.user.User;
import com.webchat.domain.user.WebChatUserDetails;
import com.webchat.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public boolean register(@RequestBody RegisterContract registerContract) {
        User newUser = new User(registerContract.getUsername(), registerContract.getEmail());
        return userService.tryRegister(newUser, registerContract.getPassword());
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return "success-logout";
    }

    @RequestMapping(value = "/postLogin", method = RequestMethod.POST)
    public String postLogin(Model model, HttpSession session) {
        // read principal out of security context and set it to session
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext()
                .getAuthentication();
        validatePrinciple(authentication.getPrincipal());
        User loggedInUser = ((WebChatUserDetails) authentication.getPrincipal()).getUser();
        model.addAttribute("currentUser", loggedInUser.getUsername());
        session.setAttribute("userId", loggedInUser.getId());
        return "success-login";
    }

    private void validatePrinciple(Object principal) {
        if (!(principal instanceof WebChatUserDetails)) {
            throw new IllegalArgumentException("Principal can not be null!");
        }
    }
}