package com.issamdrmas.dao;

import com.issamdrmas.entities.Product;
import com.issamdrmas.form.ProductForm;
import com.issamdrmas.model.ProductInfo;
import com.issamdrmas.pagination.PaginationResult;

import java.io.IOException;
import java.util.Date;
 
import javax.persistence.NoResultException;
 
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class ProductDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Product findProduct(String code) {
		try {
			String sqlString = "SELECT e FROM "+Product.class.getName() + " e WHERE e.code =:code ";
			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sqlString, Product.class);
			query.setParameter("code", code);
			return (Product) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public ProductInfo findProductInfo(String code) {
		Product product = this.findProduct(code);
		if (product == null) {
			return null;
		}
		return new ProductInfo(product.getCode(), product.getName(), product.getPrice());
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ProductForm productForm) {
		Session session = this.sessionFactory.getCurrentSession();
		String code = productForm.getCode();
		
		Product product = null;
		boolean isNew = false;
		if (code!=null) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date());
		}
		product.setCode(code);
		product.setName(productForm.getName());
		product.setPrice(productForm.getPrice());
		
		if (productForm.getFileData()!=null) {
			byte[] image = null;
			try {
				image = productForm.getFileData().getBytes();
			} catch (Exception e) {
			}
			if (image != null && image.length > 0) {
				product.setImage(image);
			}
		}
		if (isNew) {
			session.persist(product);
		}
		session.flush();
	}
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName){
		   String sql = "Select new " + ProductInfo.class.getName() //
	                + "(p.code, p.name, p.price) " + " from "//
	                + Product.class.getName() + " p ";
		   if (likeName != null && likeName.length() > 0) {
			sql += " WHERE lower(p.name) like :likeName ";
		}
		   sql += " order by p.createDate desc";
		   Session session = this.sessionFactory.getCurrentSession();
		   Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);
		   if (likeName !=null && likeName.length()>0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		   return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage){
		return queryProducts(page, maxResult, maxNavigationPage, null);
	}

}
