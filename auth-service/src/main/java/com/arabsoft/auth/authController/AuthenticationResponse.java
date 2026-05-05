package com.arabsoft.auth.authController;

import com.arabsoft.auth.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    private List<Role> roles;
    private String useLogin;
    private String matPers;
    private String codSoc;

}
