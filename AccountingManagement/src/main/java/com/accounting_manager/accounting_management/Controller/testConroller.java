package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.Entity.test;
import com.accounting_manager.accounting_management.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tests")
@Profile("nosql")
public class testConroller {

    @Autowired
    private TestService testService;

    @PostMapping("/add")
    public ResponseEntity<test> addTest(@RequestBody test newTest) {
        test createdTest = testService.addTest(newTest);
        return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
}}
