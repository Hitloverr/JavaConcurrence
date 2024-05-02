import java.util.*;

public class InstanceClose {
    /**
     * 做好封闭能让你清楚地知道域对象的访问路径，确保他是正确地同步了的.
     * Collections.synchronizedXXX就是这么做的。
     */
    private class PersonSet {
        private final Set<Person> mySet = new HashSet<>();

        public synchronized void addPerson(Person person) {
            mySet.add(person);
        }

        public synchronized boolean containsPerson(Person person) {
            return mySet.contains(person);
        }
    }

    class Person{

    }
}

/**
 *  java监视器模式 monitorenter monitorexit。内置锁
 */

class PrivateLock {
    private Object lock = new Object();

    void someMethod() {
        synchronized (lock) {
            // 私有锁的好处很多。
        }
    }
}

class CarTracker{
    private final Map<String,MutablePoint> locations;

    public CarTracker(Map<String,MutablePoint> locations) {
        this.locations = deepClone(locations);
    }


    public synchronized Map<String,MutablePoint> getLocations() {
        return deepClone(locations);
    }

    public synchronized void setLocations(String id,int x,int y) {
        MutablePoint mp = locations.get(id);
        mp.x = x;
        mp.y = y;
    }


    private Map<String,MutablePoint> deepClone(Map<String,MutablePoint> locations) {
        Map<String,MutablePoint> result = new HashMap<>();
        for (Map.Entry<String, MutablePoint> stringMutablePointEntry : locations.entrySet()) {
            result.put(stringMutablePointEntry.getKey(),stringMutablePointEntry.getValue());
        }
        return Collections.unmodifiableMap(result);
    }
}

class MutablePoint {
    public int x;
    public int y;

    public  MutablePoint() {

    }

    public MutablePoint(MutablePoint mutablePoint) {
        x = mutablePoint.x;
        y = mutablePoint.y;
    }
}