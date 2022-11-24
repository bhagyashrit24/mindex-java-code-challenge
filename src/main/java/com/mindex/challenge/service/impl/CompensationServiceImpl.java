package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    CompensationRepository compensationRepository;

    @Autowired
    EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Persisting compensation.");
        Employee employee;
        if(compensation.getEmployee().getEmployeeId() == null)
            employee = employeeService.create(compensation.getEmployee());
        else
            employee = employeeService.read(compensation.getEmployee().getEmployeeId());
        Compensation employeeWithId = new Compensation(employee, compensation.getSalary(), compensation.getEffectiveDate());
        return compensationRepository.insert(employeeWithId);
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation.");
        Employee employee = employeeService.read(id);
        Compensation compensation = compensationRepository.findByEmployee(employee);;
        if (compensation == null) {
            throw new RuntimeException("No compensation on record for employeeId: " + id);
        }
        return compensation;
    }
}
