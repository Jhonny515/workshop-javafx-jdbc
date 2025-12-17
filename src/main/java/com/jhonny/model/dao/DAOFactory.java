package com.jhonny.model.dao;

import com.jhonny.db.DB;
import com.jhonny.model.dao.impl.DepartmentDaoJDBC;
import com.jhonny.model.dao.impl.SellerDaoJDBC;

public class DAOFactory {

	public static SellerDAO createSellerDAO() {
        return (SellerDAO) new SellerDaoJDBC(DB.getConnection());
    }
	
	public static DepartmentDAO createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
