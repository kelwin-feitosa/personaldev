package com.kelwin.personaldev.presentation.exception;

public class DomainRuleException extends RuntimeException{
    public DomainRuleException(String mensagem) {
        super(mensagem);
    }
}
