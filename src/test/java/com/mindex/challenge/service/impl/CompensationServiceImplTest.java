package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    @Autowired
    CompensationServiceImpl compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String createEmployeeCompensationUrl;
    private String readEmployeeCompensationUrl;
    private String createEmployeeUrl;

    @Before
    public void setup() {
        createEmployeeUrl = "http://localhost:" + port + "/employee";
        createEmployeeCompensationUrl = "http://localhost:" + port + "/employeeCompensation";
        readEmployeeCompensationUrl = "http://localhost:" + port + "/employeeCompensation/{id}";
    }

    @Test
    public void testCreateAndReadCompensationIfEmployeeAlreadyCreated(){
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        //Created Employee
        Employee createdEmployee = restTemplate.postForEntity(createEmployeeUrl, testEmployee, Employee.class).getBody();

        Compensation testCompensation = new Compensation(testEmployee, "120000", Instant.parse("2023-01-01T00:00:00Z"));

        Compensation persistedCompensation = restTemplate.postForEntity(createEmployeeCompensationUrl, testCompensation, Compensation.class).getBody();

        //Assert for create Compensation
        assertNotNull(persistedCompensation);
        assertCompensationEquivalence(testCompensation, persistedCompensation);

        //Assert for read Compensation
        Compensation readCompensation = restTemplate.getForEntity(readEmployeeCompensationUrl, Compensation.class, persistedCompensation.getEmployee().getEmployeeId()).getBody();
        assertNotNull(readCompensation);
        assertCompensationEquivalence(testCompensation, readCompensation);
    }

    @Test
    public void testCreateAndReadCompensationIfEmployeeNotCreated(){
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Compensation testCompensation = new Compensation(testEmployee, "120000", Instant.parse("2023-01-01T00:00:00Z"));

        Compensation persistedCompensation = restTemplate.postForEntity(createEmployeeCompensationUrl, testCompensation, Compensation.class).getBody();

        //Assert for create Compensation
        assertNotNull(persistedCompensation);
        assertCompensationEquivalence(testCompensation, persistedCompensation);

        //Assert for read Compensation
        Compensation readCompensation = restTemplate.getForEntity(readEmployeeCompensationUrl, Compensation.class, persistedCompensation.getEmployee().getEmployeeId()).getBody();
        assertNotNull(readCompensation);
        assertCompensationEquivalence(testCompensation, readCompensation);
    }

    void assertCompensationEquivalence(Compensation expected, Compensation actual){
        assertEquals(expected.getEmployee().getFirstName(), actual.getEmployee().getFirstName());
        assertEquals(expected.getEmployee().getLastName(), actual.getEmployee().getLastName());
        assertEquals(expected.getEmployee().getDepartment(), actual.getEmployee().getDepartment());
        assertEquals(expected.getEmployee().getPosition(), actual.getEmployee().getPosition());
    }
}
