package com.jhonny.model.services;

import java.util.List;

import com.jhonny.model.dao.DAOFactory;
import com.jhonny.model.dao.SellerDAO;
import com.jhonny.model.entity.Seller;

public class SellerService {
    private SellerDAO sellerDAO = DAOFactory.createSellerDAO();

    public List<Seller> findAll() {
        return sellerDAO.findAll();
    }

    public void saveOrUpdate(Seller obj) {
        if (obj.getId() == null) {
            sellerDAO.insert(obj);
        } else {
            sellerDAO.update(obj);
        }
    }

    public void remove(Seller obj) {
        sellerDAO.deleteById(obj.getId());
    }

}
