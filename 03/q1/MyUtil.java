import java.util.ArrayList;
import java.util.Collections;

public class MyUtil {
    public static void printAllQueueOps(ArrayList<QueueElement> elems) {
        ArrayList<MyTriplet> ops = new ArrayList<>();
        for (QueueElement elem : elems) {
            ops.add(new MyTriplet(elem.timeEnqueued, "enq", elem.uniqueId));
            ops.add(new MyTriplet(elem.timeDequeued, "deq", elem.uniqueId));
        }

        Collections.sort(ops, (trip1, trip2) -> {
            if (trip1.time != trip2.time)
                return (int)(trip1.time - trip2.time);
            if (trip1.op.compareTo(trip2.op) != 0)
                return -trip1.op.compareTo(trip2.op);
            return 0;
        });

        for (MyTriplet op : ops)
            System.out.println(op);
    }
}

class MyTriplet {
    long time;
    String op;
    int id;

    MyTriplet(long time, String op, int id) {
        this.time = time;
        this.op = op;
        this.id = id;
    }

    @Override
    public String toString() {
        return time + " " + op + " " + id;
    }
}
