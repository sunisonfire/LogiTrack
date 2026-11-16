package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.LoginRequest;
import com.DannaS2.LogiTrack.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}