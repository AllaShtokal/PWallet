package com.shtokal.passs.service;

import com.shtokal.passs.model.Log;

public interface LogService {


    Integer getLoginStatus(String login, Boolean result, String remoteAddr);
    Log findLastSuccessful(String ipAddress, String login);
    Log findLastUnSuccessful(String ipAddress, String login);

    void resetIp(String remoteAddr);
}
