package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.LoginRequest;
import com.DannaS2.LogiTrack.dto.request.RegistroRequest;
import com.DannaS2.LogiTrack.dto.response.LoginResponse;
import com.DannaS2.LogiTrack.dto.response.RegistroResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    RegistroResponse registrar(RegistroRequest request);
}
