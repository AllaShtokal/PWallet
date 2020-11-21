package com.shtokal.passs.repository;

import com.shtokal.passs.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    Log findFirstByIpAddress(String ipAddress);

}
