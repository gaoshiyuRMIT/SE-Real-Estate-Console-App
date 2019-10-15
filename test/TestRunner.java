import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(
            property.TestRentalProperty.class,
            se.TestApplication.class,
            se.TestBranch.class,
            user.customer.TestNonOwner.class
        );
        System.out.printf("Tests Run: %d\n", result.getRunCount());
        System.out.printf("Failures: %d\n", result.getFailureCount());
        for (Failure failure : result.getFailures()) {
            for(int i = 0; i < 60; i++) {
                System.out.print("=");
            }
            System.out.println("");
            System.out.println(failure.toString());
            String trace = failure.getTrace();
            String[] lines = trace.split("\n", 5);
            trace = String.format("%s\n%s\n%s\n%s\n...", lines[0], lines[1], lines[2],
                                    lines[3]);
            System.out.println(trace);
        }
    }
}