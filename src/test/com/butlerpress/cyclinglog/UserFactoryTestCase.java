package com.butlerpress.cyclinglog;

public class UserFactoryTestCase extends TestCase {

  public void testSave() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

    // User
    User user = new User("test", "Test User");
    userFactory.save(user);
    assertNotNull(user.getName() + " should be saved to database (id != null)", user.getId());

    // Cyclist
    User cyclist = new User("cyclist", "Test Cyclist");
    cyclist.addAdministrator(user);
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

    assertEquals("Users in database", 2, userFactory.findAll().size());

    User cyclistFromDB = userFactory.find(cyclist.getUsername());
    assertEquals("Cyclist name", cyclist.getName(), cyclistFromDB.getName());
    assertNotNull("Administrator list", cyclistFromDB.getAdministrators());
    assertEquals("Administrator list", 1, cyclistFromDB.getAdministrators().size());
    assertTrue("Admin in list for cyclist", cyclistFromDB.getAdministrators().contains(user));
  }
}
