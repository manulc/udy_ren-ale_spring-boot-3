package com.mlorenzo.besttravel.services;

import java.util.Map;
import java.util.Set;

public interface UserService {
    Map<String, Boolean> enableOrDisable(String username);
    Map<String, Set<String>> addRole(String username, String role);
    Map<String, Set<String>> deleteRole(String username, String role);
}
