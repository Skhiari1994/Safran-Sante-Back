package com.arabsoft.auth.reset_password;

import lombok.Data;

@Data
public class PasswordResetRequest {

    private String useLogin ;
    private String newPassword ;
    private String confirmPassword ;
}
