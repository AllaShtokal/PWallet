package com.shtokal.passs.repository;
import com.shtokal.passs.model.FunctionRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunctionRunRepository extends JpaRepository<FunctionRun, Long> {

    List<FunctionRun> findAllByUser_Login(String login);


}
