package com.dioncanolli.cpulse_back_end.controller;

import com.dioncanolli.cpulse_back_end.dto.UserDTO;
import com.dioncanolli.cpulse_back_end.entity.User;
import com.dioncanolli.cpulse_back_end.service.MyService;
import com.dioncanolli.cpulse_back_end.utility.JWTTokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final MyService myService;

    public UserController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping(value = "/admin/users/all")
    public ResponseEntity<List<UserDTO>> findAllUsers(){
        List<UserDTO> userDTOS = myService.findAllUsers();
        return (userDTOS.isEmpty())
                ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/verify")
    public int verifyAdminLoggedIn(){
        return 1;
    }

    @GetMapping(value = "/users/verify")
    public int verifyUserLoggedIn(){
        return 1;
    }

    @GetMapping(value = "/users/one")
    public ResponseEntity<UserDTO> findUser(@RequestHeader(value = "Authorization") String jwtToken){
        UserDTO userDTO = myService.findUserDTOByEmail(JWTTokenGenerator.extractUsernameFromToken(jwtToken));
        return (userDTO == null)
                ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/users/update")
    public ResponseEntity<Boolean> updateUser(@RequestBody User user){
        boolean result = myService.modifyUser(user);
        return !result
                ? new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping(value = "/users/logout")
    public ResponseEntity<Boolean> logout(@RequestHeader(value = "Authorization") String jwtToken){
        try{
            myService.blacklistToken(jwtToken);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/user/delete")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader(value = "Authorization") String jwtToken){
        boolean result = myService.deleteUser(JWTTokenGenerator.extractUsernameFromToken(jwtToken));
        return !result ?
                new ResponseEntity<>(false, HttpStatus.BAD_REQUEST) : new ResponseEntity<>(true, HttpStatus.OK);
    }
}


























