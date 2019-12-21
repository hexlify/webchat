package com.webchat.rest;

import com.webchat.dto.statistics.UserStatsDTO;
import com.webchat.dto.user.AdminUserDTO;
import com.webchat.model.User;
import com.webchat.rest.errors.exceptions.BadRequestException;
import com.webchat.rest.errors.exceptions.UserNotFoundException;
import com.webchat.service.UserService;
import com.webchat.statistics.StatisticsService;
import com.webchat.statistics.UserStats;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final StatisticsService statisticsService;

    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper,
                           StatisticsService statisticsService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.statisticsService = statisticsService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        List<User> users = userService.getAll();
        List<AdminUserDTO> userDTOs = users.stream()
                .map(x -> modelMapper.map(x, AdminUserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping(value = "/ban/{id}")
    public void banUser(@PathVariable("id") User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }

        boolean wasBanned = userService.tryBan(user);
        if (!wasBanned) {
            throw new BadRequestException("Can't ban admin");
        }
    }

    @GetMapping(value = "/activate/{id}")
    public void activateUser(@PathVariable("id") User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }

        userService.activate(user);
    }

    @GetMapping(value = "/stats/{username}")
    public ResponseEntity<UserStatsDTO> getStats(@PathVariable("username") User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }

        UserStats userStats = statisticsService.getUserStatistics(user);
        return ResponseEntity.ok(modelMapper.map(userStats, UserStatsDTO.class));
    }
}
