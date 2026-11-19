package com.arabsoft.auth.authController;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String useLogin;

    private String cin;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Societe (cod_soc) is mandatory")
    @JsonProperty("cod_soc")
    private String codSoc;

    public String getIdentifiant() {
        if (cin != null && !cin.isBlank()) {
            return cin.trim();
        }
        return useLogin != null ? useLogin.trim() : "";
    }

}