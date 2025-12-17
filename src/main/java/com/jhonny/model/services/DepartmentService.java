package com.jhonny.model.services;

import java.util.List;

import com.jhonny.model.dao.DAOFactory;
import com.jhonny.model.dao.DepartmentDAO;
import com.jhonny.model.entity.Department;

public class DepartmentService {

    private DepartmentDAO departmentDAO = DAOFactory.createDepartmentDao();

    public List<Department> findAll() {
        return departmentDAO.findAll();
    }

}
