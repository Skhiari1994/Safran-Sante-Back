package com.arabsoft.gestionindemnites.DTO;

import lombok.Data;

@Data

public class VirCarteFileResponse {
    private String message;
    private Long seq;
    private String fileContent; // base64 encoded file content
    // getters & setters
}
