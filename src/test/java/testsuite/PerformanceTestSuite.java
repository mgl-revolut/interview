package testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import service.PerformanceMoneyTransferTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    PerformanceMoneyTransferTest.class,
})
public class PerformanceTestSuite {
}
