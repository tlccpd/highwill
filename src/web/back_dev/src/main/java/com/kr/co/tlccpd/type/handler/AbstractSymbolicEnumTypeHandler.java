package com.company.core.type.handler;


import java.sql.SQLException;
import java.sql.Types;
import java.util.EnumSet;

import com.company.core.type.Symbolic;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;


public abstract class AbstractSymbolicEnumTypeHandler<E extends Enum<E>> implements TypeHandlerCallback {

   
   private EnumSet<E> symbolicEnumSet;
   

   public AbstractSymbolicEnumTypeHandler(Class<E> symbolicEnumClass){
      if(!Symbolic.class.isAssignableFrom(symbolicEnumClass))
         throw new RuntimeException("Class should be '" + Symbolic.class.getName() + "' type - "+symbolicEnumClass.getName());
      symbolicEnumSet = EnumSet.allOf(symbolicEnumClass);
   }
   
   public Object getResult(ResultGetter getter) throws SQLException {
      String result = getter.getString();
      if (getter.wasNull())
         return null;
      return valueOf(result);
   }
   
   public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
      if (parameter == null)
         setter.setNull(Types.VARCHAR);
      else
         setter.setString(((Symbolic)parameter).getSymbol());
   }

   public E valueOf(String value) {
      for(E e: symbolicEnumSet){
         if(((Symbolic)e).getSymbol().equals(value))
            return e;
      }
      return null;
   }
}