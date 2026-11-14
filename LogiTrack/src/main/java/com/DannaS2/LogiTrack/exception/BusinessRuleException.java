package com.DannaS2.LogiTrack.exception;

public class BusinessRuleException extends RuntimeException{
    public BusinessRuleException(String mensaje){
        super(mensaje);
    }
}