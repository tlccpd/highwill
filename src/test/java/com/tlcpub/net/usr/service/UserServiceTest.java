package com.tlcpub.net.usr.service;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;

import javax.management.relation.Role;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.company.core.exception.AlreadyExistException;
import com.company.core.exception.NotExistException;
import com.company.core.exception.NotFoundException;
import com.tlcpub.net.usr.userDto.User;


@ContextConfiguration(locations={
      "classpath:datasource.xml",
      "classpath:comp-user-context.xml",
      "classpath:comp-test-context.xml"})
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {


   @Autowired
   UserService userService;

   static User baseUser;
   static User updateUser;
   static User anotherUser;


   @org.junit.BeforeClass
   public static void setUpOnce() throws Exception{

      System.out.println("# setUpOnce()");

      Domain testDomain = (Domain)ObjectLoader.load(Domain.class);

      baseUser = testDomain.getBaseUser();
      updateUser = testDomain.getUpdateUser();
      anotherUser = testDomain.getAnotherUser();

      baseUser.setRole(Role.MEMBER);
      updateUser.setRole(Role.MEMBER);
      anotherUser.setRole(Role.ADMIN);
   }

   @org.junit.AfterClass
   public static void tearDownOnce() {
      System.out.println("# tearDownOnce()");
   }

   @org.junit.Before
   public void setUp(){
      logger.info("# setUp()");
   }

   @org.junit.After
   public void tearDown(){
      logger.info("# tearDown()");
   }

   @org.junit.Test
   public void testAllServiceMethods() throws Exception{

      Exception expectedException = null;

      // 1. subscribe()

      userService.subscribe(baseUser);

      // 2. subscribe() - AlreadyExistException

      try{
         userService.subscribe(baseUser);
      }catch(Exception e){
         expectedException = e;
      }
      assertNotNull(expectedException);
      assertTrue(expectedException instanceof AlreadyExistException);

      // 3. getProfile()

      User foundUser = userService.getProfile(baseUser.getId());
      assertNotNull(foundUser);
      assertEquals(baseUser.getId(), foundUser.getId());
//      assertEquals(baseUser.getPasswd(), foundUser.getPasswd());
      assertEquals(baseUser.getName(), foundUser.getName());
      assertEquals(baseUser.getEmail(), foundUser.getEmail());

      // 4. getProfile() - NotFoundException

      try{
         userService.getProfile(anotherUser.getId());
      }catch(Exception e){
         expectedException = e;
      }
      assertNotNull(expectedException);
      assertTrue(expectedException instanceof NotFoundException);

      // 5. modifyProfile()

      userService.modifyProfile(updateUser);

      // 6. getProfile() - Compare with updated profile

      foundUser = userService.getProfile(baseUser.getId());
      assertNotNull(foundUser);
      assertEquals(baseUser.getId(), foundUser.getId());
//      assertEquals(updateUser.getPasswd(), foundUser.getPasswd());
      assertEquals(updateUser.getName(), foundUser.getName());
      assertEquals(updateUser.getEmail(), foundUser.getEmail());

      // 7. modifyProfile() - NotExistException

      expectedException = null;
      try{
         userService.modifyProfile(anotherUser);
      }catch(NotExistException e){
         expectedException = e;
      }
      assertNotNull(expectedException);
      assertTrue(expectedException instanceof NotExistException);

      // 8. unsubscribe() - NotExistException

      expectedException = null;
      try{
         userService.unsubscribe(anotherUser.getId());
      }catch(Exception e){
         expectedException = e;
      }
      assertNotNull(expectedException);
      assertTrue(expectedException instanceof NotExistException);

      // 9. unsubscribe()

      userService.unsubscribe(baseUser.getId());

      // 10. getProfile() -  NotFoundException

      expectedException = null;
      try{
         userService.getProfile(baseUser.getId());
      }catch(Exception e){
         expectedException = e;
      }
      assertNotNull(expectedException);
      assertTrue(expectedException instanceof NotFoundException);

      // 11. getEntireManagerProfiles()
      
      userService.subscribe(anotherUser);

      List<User> userList = userService.getEntireManagerProfiles();
      assertNotNull(userList);
      assertTrue(userList.size() >= 1);      
   }
}
