package com.jhonny.model.dao;

import java.util.List;

import com.jhonny.model.entity.Department;
import com.jhonny.model.entity.Seller;

public interface SellerDAO {

	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();
	List<Seller> findByDepartment(Department department);
}
