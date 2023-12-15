package ru.softlab.efr.services.auth.utils.model;

import ru.softlab.efr.services.auth.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeParseResult {
    private List<Employee> parsedEmployees = new ArrayList<>();
    private List<ParseError> errors = new ArrayList<>();

    public List<Employee> getParsedEmployees() {
        return parsedEmployees;
    }

    public void setParsedEmployees(List<Employee> parsedEmployees) {
        this.parsedEmployees = parsedEmployees;
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public void setErrors(List<ParseError> errors) {
        this.errors = errors;
    }
}
