package com.jhonny.model.dao;

import java.util.List;

import com.jhonny.model.entity.Department;

public interface DepartmentDAO {

	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
}
