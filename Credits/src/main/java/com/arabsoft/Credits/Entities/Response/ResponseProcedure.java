package com.arabsoft.Credits.Entities.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ResponseProcedure {
    private String message;
    private List<String> files; // Added this field
    private boolean success;

}
