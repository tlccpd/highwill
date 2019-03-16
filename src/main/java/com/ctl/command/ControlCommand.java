package com.ctl.command;


public enum ControlCommand {


   CacheReload(
      new CacheReloadEvent(null)),

   Rescheduling(
      new ReschedulingEvent(null));

   private ApplicationEvent event;


   ControlCommand(ApplicationEvent event){
      this.event = event;
   }

   public ApplicationEvent getEvent() {
      return event;
   }
}