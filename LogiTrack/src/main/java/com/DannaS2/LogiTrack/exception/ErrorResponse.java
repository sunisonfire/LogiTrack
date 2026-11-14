package com.DannaS2.LogiTrack.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String message, String errorCode){
}