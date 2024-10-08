package com.example.trung.entityusers.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomException extends RuntimeException{
    private ErrorStatus errorStatus;
}
