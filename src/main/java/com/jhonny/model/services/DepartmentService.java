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

    public void saveOrUpdate(Department obj) {
        if (obj.getId() == null) {
            departmentDAO.insert(obj);
        } else {
            departmentDAO.update(obj);
        }
    }

    public void remove(Department obj) {
        departmentDAO.deleteById(obj.getId());
    }

}
