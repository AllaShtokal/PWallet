package com.shtokal.passs.service;

import com.shtokal.passs.dto.DataChangeResponse;
import com.shtokal.passs.model.DataChange;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DataChangeService {

    List<DataChangeResponse> findAllByUserLoginAndPasswordId(String login, String passwordId);
    DataChange findById (String id);
}
