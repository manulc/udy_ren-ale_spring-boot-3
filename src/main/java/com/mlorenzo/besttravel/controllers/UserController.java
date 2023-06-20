package com.mlorenzo.besttravel.controllers;

import com.mlorenzo.besttravel.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @PatchMapping("{username}/enable-or-disable")
    public Map<String, Boolean> enableOrDisable(@PathVariable String username) {
        return userService.enableOrDisable(username);
    }

    @PatchMapping("{username}/add-role")
    public Map<String, Set<String>> addRole(@PathVariable String username, @RequestParam("value") String role) {
        return userService.addRole(username, role);
    }

    @PatchMapping("{username}/remove-role")
    public Map<String, Set<String>> removeRole(@PathVariable String username, @RequestParam("value") String role) {
        return userService.deleteRole(username, role);
    }
}
