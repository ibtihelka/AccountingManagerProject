package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.Entity.test;
import com.accounting_manager.accounting_management.Repository.testRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@Profile("nosql")
public class TestService {
    @Autowired
    private testRepo testRepository;

    public test addTest(test newTest) {
        return testRepository.save(newTest);
    }
}
