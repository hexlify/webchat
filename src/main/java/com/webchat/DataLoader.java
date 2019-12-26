package com.webchat;

import com.webchat.config.props.AppProperties;
import com.webchat.model.ChatRoom;
import com.webchat.model.Role;
import com.webchat.model.User;
import com.webchat.repository.ChatRoomRepository;
import com.webchat.repository.RoleRepository;
import com.webchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final AppProperties appProperties;

    @Autowired
    public DataLoader(UserService userService, RoleRepository roleRepository, ChatRoomRepository chatRoomRepository,
                      AppProperties appProperties) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.appProperties = appProperties;
    }

    private void createRoles() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");

        roleRepository.saveAll(Arrays.asList(adminRole, userRole));
    }

    private void createTestUsers() {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@email.net");
        user.setFirstName("Yegor");
        user.setPassword("123");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@email.net");
        user2.setFirstName("Vovan");
        user2.setPassword("123");

        userService.registerUser(user);
        userService.registerUser(user2);
    }

    private void createAdmin() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@email.net");
        admin.setFirstName("Yegor");
        admin.setPassword(appProperties.getAdminPassword());

        userService.registerAdmin(admin);
    }

    private void createChatRooms() {
        ChatRoom chatRoom1 = new ChatRoom("Test1", "Test1 description");
        ChatRoom chatRoom2 = new ChatRoom("Test2", "Test2 description");

        chatRoomRepository.saveAll(Arrays.asList(chatRoom1, chatRoom2));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createRoles();
        createChatRooms();
        createTestUsers();
        createAdmin();
    }
}
