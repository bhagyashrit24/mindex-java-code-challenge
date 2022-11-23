package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    @Autowired
    ReportingStructureServiceImpl reportingStructureServiceImpl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String reportingStructureIdUrl;
    private String employeeUrl;

    @Before
    public void setup() {
        reportingStructureIdUrl = "http://localhost:" + port + "/reportingStructure/{id}";
        employeeUrl = "http://localhost:" + port + "/employee";
    }

    @Test
    public void testCreateReportingStructureZeroDR(){
        Employee testEmployee = createEmployee("John", "Doe");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        ReportingStructure expected = new ReportingStructure(testEmployee, 0);

        ReportingStructure createdRS = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();

        assertNotNull(createdRS.getEmployee());
        assertEquals(expected.getNoOfReports(), createdRS.getNoOfReports());
    }

    @Test
    public void testCreateReportingStructureTwoDR(){
        Employee reportEmployee1 = createEmployee("Paul", "Maccarthy");
        Employee reportEmployee2 = createEmployee("Pete", "Best");
        Employee testEmployee = createEmployee("John", "Doe");
        List<Employee> dr = new ArrayList<>();
        dr.add(reportEmployee1);
        dr.add(reportEmployee2);
        testEmployee.setDirectReports(dr);
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        ReportingStructure expected = new ReportingStructure(testEmployee, 2);

        ReportingStructure createdRS = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull(createdRS.getEmployee());
        assertEquals(expected.getNoOfReports(), createdRS.getNoOfReports());
    }

    @Test
    public void testCreateReportingStructureFiveDR(){
        Employee reportEmployee1 = createEmployee("Paul", "Maccarthy");
        Employee reportEmployee2 = createEmployee("Pete", "Best");
        Employee reportEmployee21 = createEmployee("Penelope", "Alvarez");
        Employee reportEmployee22 = createEmployee("Julia", "Roberts");
        List<Employee> dr2 = new ArrayList<>();
        dr2.add(reportEmployee21);
        dr2.add(reportEmployee22);
        reportEmployee1.setDirectReports(dr2);
        Employee reportEmployee221 = createEmployee("Dave", "Smith");
        List<Employee> dr22 = new ArrayList<>();
        dr22.add(reportEmployee221);
        reportEmployee22.setDirectReports(dr22);
        Employee testEmployee = createEmployee("John", "Doe");
        List<Employee> dr1 = new ArrayList<>();
        dr1.add(reportEmployee1);
        dr1.add(reportEmployee2);
        testEmployee.setDirectReports(dr1);
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        ReportingStructure expected = new ReportingStructure(testEmployee, 5);

        ReportingStructure createdRS = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull(createdRS.getEmployee());
        assertEquals(expected.getNoOfReports(), createdRS.getNoOfReports());
    }

    Employee createEmployee(String firstName, String lastName){
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setPosition("Developer");
        employee.setDepartment("Engineering");
        return employee;
    }
}

