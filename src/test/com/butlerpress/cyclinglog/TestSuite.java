package com.butlerpress.cyclinglog;

import junit.framework.Test;

public class TestSuite extends junit.framework.TestSuite {

    public static Test suite() {

      TestSuite suite = new TestSuite();

      suite.addTestSuite(EquipmentControllerTestCase.class);
      suite.addTestSuite(EquipmentFactoryTestCase.class);
      suite.addTestSuite(EquipmentFormValidatorTestCase.class);
      suite.addTestSuite(LoginControllerTestCase.class);
      suite.addTestSuite(UserFactoryTestCase.class);
      suite.addTestSuite(WeekControllerTestCase.class);
      suite.addTestSuite(WeeksFactoryTestCase.class);
      suite.addTestSuite(WeekTestCase.class);
      suite.addTestSuite(WorkoutFactoryTestCase.class);
      suite.addTestSuite(WorkoutsControllerTestCase.class);
      suite.addTestSuite(WorkoutTestCase.class);
		
      return suite;
    }
    
}
