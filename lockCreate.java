/**
锁像 synchronized 同步块一样，是一种线程同步机制，但比 Java 中的 synchronized 同步块更复杂。因为锁（以及其它更高级的线程同步机制）是由 synchronized 同步块的方式实现的，所以我们还不能完全摆脱 synchronized 关键字
*/

//一个简单的锁
public class Counter{
    private int count = 0;

    public int inc(){
        synchronized(this){
            return ++count;
        }
    }
}
/**
可以看到在 inc()方法中有一个 synchronized(this)代码块。该代码块可以保证在同一时间只有一个线程可以执行 return ++count。虽然在 synchronized 的同步块中的代码可以更加复杂，但是++count 这种简单的操作已经足以表达出线程同步的意思。
*/

/**
Lock()类
lock()方法会对 Lock 实例对象进行加锁，因此所有对该对象调用 lock()方法的线程都会被阻塞，直到该 Lock 对象的 unlock()方法被调用。
*/
public class Counter{
    private Lock lock = new Lock();
    private int count = 0;

    public int inc(){
        lock.lock();
        int newCount = ++count;
        lock.unlock();
        return newCount;
    }
}

public class Counter{
public class Lock{
    private boolean isLocked = false;

    public synchronized void lock()
        throws InterruptedException{
        while(isLocked){
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock(){
        isLocked = false;
        notify();
    }
}