package com.shtokal.passs.service;

import com.shtokal.passs.model.Log;
import com.shtokal.passs.repository.LogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class LogServiceImplementation implements LogService {

    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    public LogServiceImplementation(LogRepository logRepository, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Integer getLoginStatus(Boolean result, String remoteAddr) {

        Log logByIp = logRepository.findFirstByIpAddress(remoteAddr);
        if (logByIp != null) {
            if (logByIp.getAttempt() == 4) {
                return 4;
            } else if (logByIp.getAttempt() == 0) {
                if (result) return 0;
                else {
                    logByIp.setAttempt(1);
                    logRepository.save(logByIp);
                    return 1;
                }


            } else {
                if (timeChek(logByIp)) {
                    if (result) {
                        logByIp.setAttempt(0);
                        logRepository.save(logByIp);
                        return 0;
                    } else {

                        if (logByIp.getAttempt() == 3) {
                            logByIp.setAttempt(4);
                            logRepository.save(logByIp);
                            return 4;
                        } else {
                            int attempt = logByIp.getAttempt();
                            attempt++;
                            logByIp.setAttempt(attempt);
                            logRepository.save(logByIp);
                            return attempt;

                        }
                    }

                } else {
                    return logByIp.getAttempt();
                }
            }

        } else {

            return createLog(result, remoteAddr);
        }

    }

    private Boolean timeChek(Log logByIp) {
        LocalDateTime time = logByIp.getTime();
        int attempt = logByIp.getAttempt();
        if(attempt==1 && LocalDateTime.now().isAfter(time.plusSeconds(5))){ return true;}
        else
        if(attempt==2 && LocalDateTime.now().isAfter(time.plusSeconds(10))){ return true;}
        else
        if(attempt==3 && LocalDateTime.now().isAfter(time.plusMinutes(2))){ return true;}
        else return false;

    }

    private Integer createLog(Boolean result, String remoteAddr) {
        Log log = new Log();
        log.setIpAddress(remoteAddr);
        log.setTime(LocalDateTime.now());
        if (result) {
            log.setAttempt(0);
            logRepository.save(log);
            return 0;
        } else {
            log.setAttempt(1);
            logRepository.save(log);
            return 1;
        }


    }


}
