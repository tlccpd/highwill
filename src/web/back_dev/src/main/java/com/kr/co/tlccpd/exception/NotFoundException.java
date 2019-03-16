package com.company.core.exception;


public class NotFoundException extends ApplicationException {


   static final long serialVersionUID = 1;
   
   
   public NotFoundException(Object source){
      super(source);
   }
}
