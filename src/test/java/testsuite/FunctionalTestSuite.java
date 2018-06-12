package testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import service.AccountServiceTest;
import service.CustomerServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AccountServiceTest.class,
    CustomerServiceTest.class
})
public class FunctionalTestSuite {
}
