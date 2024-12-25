//package com.example.Disaster_Management_Tool.Controllers;
//
//import com.example.Disaster_Management_Tool.Dto.UserRequest;
//import com.example.Disaster_Management_Tool.Entities.User;
//import com.example.Disaster_Management_Tool.Services.UserServices;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/auth")
//public class UserController {
//    @Autowired
//    private UserServices userServices;
//
//    @GetMapping("/getAll")
//    public ResponseEntity<List<User>> getAllUsers(){
//        List<User> userList = userServices.getAllUsers();
//        return new ResponseEntity<>(userList, HttpStatus.OK);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest){
//        User userRequest1 = userServices.createUser(userRequest);
//        return new ResponseEntity<>(userRequest1.getEmail(), HttpStatus.CREATED);
//    }
//}
