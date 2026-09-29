package com.kelwin.personaldev.presentation.exception;

public class BusinessRuleException extends RuntimeException{
    public BusinessRuleException(String mensagem) {
        super(mensagem);
    }
}
