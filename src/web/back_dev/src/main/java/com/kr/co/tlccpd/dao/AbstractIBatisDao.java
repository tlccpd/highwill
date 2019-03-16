package com.company.core.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientTemplate;


public abstract class AbstractIBatisDao {


   @Autowired
   protected SqlMapClientTemplate template;

}
