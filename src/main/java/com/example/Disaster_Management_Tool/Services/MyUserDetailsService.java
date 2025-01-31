package com.example.Disaster_Management_Tool.Services;

import com.example.Disaster_Management_Tool.Entities.User;
import com.example.Disaster_Management_Tool.Entities.UserPrincipal;
import com.example.Disaster_Management_Tool.Repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service
//public class UserServices {
//    @Autowired
//    private UserRepo userRepo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    public List<User> getAllUsers() {
//        return userRepo.findAll();
//    }
//
//    public User createUser(UserRequest userRequest) {
//        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
//        System.out.println(encodedPassword);
//        User user = new User();
//        user.setUsername(userRequest.getUsername());
//        user.setPassword(encodedPassword);
//        user.setEmail(userRequest.getEmail());
//        User savedUser = userRepo.save(user);
//        return savedUser;
//    }
//
//    public User findByEmail(String username) {
//        return userRepo.findByEmail(username);
//    }
//
//    public User getCurrentUser() {
//        // Get the currently authenticated user's username
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        String username = authentication.getName();
//
//        // Fetch the user from the database using the username
//        return userRepo.findByUsername(username);
//
//    }
//}
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);



    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findByUsername(username);
//        if (user == null) {
//            System.out.println("User Not Found");
//            throw new UsernameNotFoundException("user not found");
//        }
//
//        return new UserPrincipal(user);
//    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Log the username to trace the authentication process
        logger.info("Attempting to load user by username: {}", username);

        User user = userRepo.findByUsername(username);
        if (user == null) {
            logger.error("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Return the UserPrincipal which implements UserDetails
        return new UserPrincipal(user);
    }
}
