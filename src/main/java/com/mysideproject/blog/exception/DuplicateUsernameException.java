package com.mysideproject.blog.exception;

/**
*
* @return This exception extends RuntimeException.
* 
*/

public class DuplicateUsernameException extends RuntimeException {
    
    // 繼承 RuntimeException 是 Spring Boot 的慣例，可以被 Spring 框架自動處理。
    public DuplicateUsernameException(String message) {
        super(message);
    }
}