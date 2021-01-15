package com.shtokal.passs.repository;

import com.shtokal.passs.model.DataChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataChangeRepository extends JpaRepository<DataChange, Long> {

    List<DataChange> findAllByUser_LoginAndPassword_Id(String login, Long passwordId);
}
