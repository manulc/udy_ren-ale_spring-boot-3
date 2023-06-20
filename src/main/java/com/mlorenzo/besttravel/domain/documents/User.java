package com.mlorenzo.besttravel.domain.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String dni;
    private String username;
    private String password;
    private Boolean enabled;
    private Set<String> roles;
}
