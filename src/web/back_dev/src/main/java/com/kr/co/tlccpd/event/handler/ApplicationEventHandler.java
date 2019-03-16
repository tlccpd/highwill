package com.company.core.event.handler;


import org.springframework.context.ApplicationEvent;


public interface ApplicationEventHandler {

   
   public abstract void handle(ApplicationEvent event) throws Exception;
   
}
