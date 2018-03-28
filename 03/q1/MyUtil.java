import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyUtil {
    public static void printAllQueueOps(ArrayList<QueueElement> elems) {
         SortedMap<Long, String> ops = new TreeMap<>();
         for (QueueElement elem: elems) {
             ops.put(elem.timeEnqueued, "enq " + elem.uniqueId);
             ops.put(elem.timeDequeued, "deq " + elem.uniqueId);
         }

         for (Long opTime : ops.keySet()) {
             System.out.println(ops.get(opTime) + "\t\t" + opTime);
         }
    }
}
