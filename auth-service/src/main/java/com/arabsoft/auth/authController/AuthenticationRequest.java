package com.arabsoft.auth.authController;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
public class AuthenticationRequest {

    @NotBlank(message = "Use login (matricule) is mandatory")
    private String useLogin;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;

    @NotBlank(message = "Societe (cod_soc) is mandatory")
    @JsonProperty("cod_soc")
    private String codSoc;

}