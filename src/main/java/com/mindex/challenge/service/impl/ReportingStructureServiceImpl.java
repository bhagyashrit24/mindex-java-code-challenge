package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService{
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @Override
    public ReportingStructure getReportingStructure(String id){
        LOG.debug("Creating reporting structure for employee Id [{}]", id);
        Employee employee = employeeServiceImpl.read(id);
        int totalNumberOfReports = getNoOfReports(employee);

        return new ReportingStructure(employee, totalNumberOfReports);
    }

    private int getNoOfReports(Employee employee){
        return calculate(employee) - 1;
    }

    private int calculate(Employee employee){
        int count=0;
        List<Employee> directReports = employee.getDirectReports();
        if(directReports != null){
            for(int i=0;i<directReports.size();i++){
                count += calculate(directReports.get(i));
            }
        }
        return count+1;
    }
}
