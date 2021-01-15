package com.shtokal.passs.controller;

import com.shtokal.passs.dto.DataChangeResponse;
import com.shtokal.passs.dto.FunctionRunResponse;
import com.shtokal.passs.model.DataChange;
import com.shtokal.passs.model.FunctionRun;
import com.shtokal.passs.repository.FunctionRunRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/function")
public class FunctionRunController {

    private final FunctionRunRepository functionRunRepository;
    private final ModelMapper modelMapper;

    public FunctionRunController(FunctionRunRepository functionRunRepository, ModelMapper modelMapper) {

        this.functionRunRepository = functionRunRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<FunctionRunResponse>> getAllByLogin(@RequestParam("login") String login) {
        if (login == null) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        List<FunctionRun> functionRuns = functionRunRepository.findAllByUser_Login(login);
        List<FunctionRunResponse> functionRunResponses = new ArrayList<>();
        for (FunctionRun functionRun : functionRuns) {
            FunctionRunResponse map = modelMapper.map(functionRun, FunctionRunResponse.class);
            functionRunResponses.add(map);
        }

        return new ResponseEntity<>(functionRunResponses, headers, HttpStatus.OK);

    }
}
