package com.jdc.phoneshop.admin.model;

import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.admin.model.entity.Employee.Role;
import com.jdc.phoneshop.admin.model.imp.EmployeeModelImp;

public interface EmployeeModel {

    void create(Employee emp);

    Employee findById(int id);

    Set<Employee> getAll();

    void unactivate(Employee emp);
    
    List<Employee> find(Role role, String name);
    
    public static EmployeeModel getModel() {
    	return new EmployeeModelImp();
    }

}