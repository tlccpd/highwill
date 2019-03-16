package com.company.core.exception;


public class AlreadyExistException extends ApplicationException {


   static final long serialVersionUID = 1;
   
   
   public AlreadyExistException(Object source){
      super(source);
   }
}
