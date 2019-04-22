import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;

public class TestIntersection {
    public static void main(String[] args) throws IOException {
        testFromStdin();
        testLocal();
    }

    public static void testLocal() throws IOException {
        System.out.println("Local Test Cases");
        System.out.println("----------------");

        TestCase[] testCases = new TestCase[] {
            () -> {
                Edge e1 = new Edge(new Point(1.0,0.0), new Point(0.9607784665925301,0.7618828744738902));
                Edge e2 = new Edge(new Point(1.0,1.0), new Point(0.9606856232338309,0.22037147596411788));
                boolean expected = true;

                return e1.intersects(e2) == expected;
            }
        };

        int i = 0;
        int passed = 0;
        for (TestCase testCase : testCases) {
            i++;

            if (!testCase.test()) {
                System.out.println(i+" failed");
            } else {
                System.out.println(i+" passed");
                passed++;
            }
        }
        System.out.println("\n"+passed+"/"+i+" passed");
    }

    public static void testFromStdin() throws IOException {
        System.out.println("Stdin Test Cases");
        System.out.println("----------------");

        ArrayList<TestCase> testCases = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] strs = line.split(",");
            Double[] dubs = new Double[8];
            for (int i = 0; i < 8; i++)
                dubs[i] = Double.parseDouble(strs[i]);

            Point p1 = new Point(dubs[0], dubs[1]);
            Point p2 = new Point(dubs[2], dubs[3]);
            Point p3 = new Point(dubs[4], dubs[5]);
            Point p4 = new Point(dubs[6], dubs[7]);
            Edge e1 = new Edge(p1, p2);
            Edge e2 = new Edge(p3, p4);

            boolean expectedResult = Boolean.parseBoolean(strs[8]);

            testCases.add(() -> {
                boolean success = (e1.intersects(e2) == expectedResult);
                if (!success) {
                    String s = ""
                        + "Edge(("+dubs[0]+","+dubs[1]+"), ("+dubs[2]+","+dubs[3]+")), "
                        + "Edge(("+dubs[4]+","+dubs[5]+"), ("+dubs[6]+","+dubs[7]+")), "
                        + "result!="+expectedResult;
                    System.out.println(s);
                }
                return success;
            });
        }

        int i = 0;
        int passed = 0;
        for (TestCase testCase : testCases) {
            i++;

            if (!testCase.test()) {
                System.out.println(i+" failed");
            } else {
                System.out.println(i+" passed");
                passed++;
            }
        }
        System.out.println("\n"+passed+"/"+i+" passed");
    }
}

/**
 * Functional Interface
 */
interface TestCase {
    public boolean test();
}
