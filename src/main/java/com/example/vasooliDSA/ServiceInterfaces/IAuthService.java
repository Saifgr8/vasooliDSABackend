package com.example.vasooliDSA.ServiceInterfaces;

import com.example.vasooliDSA.DTO.DTOLoginRequest;
import com.example.vasooliDSA.DTO.DTOLoginResponse;
import com.example.vasooliDSA.DTO.DTORegisterRequest;
import com.example.vasooliDSA.Models.User;

public interface IAuthService {
    User registerUser(DTORegisterRequest request);
    DTOLoginResponse loginUserViaPassword(DTOLoginRequest request);
}
