package thread.lock;

import java.util.HashMap;
import java.util.Map;

public class ReadWriteLock {

    private Map<Thread, Integer> readingThreads = new HashMap<>();
    private int writeAccesses = 0;
    private int writeRequests = 0;
    private Thread wringThread = null;

    public synchronized void lockRead() throws InterruptedException {

        Thread callingThread = Thread.currentThread();//获取当前运行线程
        while (!canGrantReadAccess(callingThread)){//写线程及写线程请求存在,读线程被阻塞
            wait();
        }
        readingThreads.put(callingThread, (getReadAccessCount(callingThread) + 1));

    }

    public synchronized void unlockRead(){

        Thread callingThread = Thread.currentThread();
        if (!isReader(callingThread)){
            throw new IllegalMonitorStateException(
                    "Calling Thread does not" +
                            "hold a read lock on this ReadWriteLock"
            );
        }
        int accessCount = getReadAccessCount(callingThread);
        if (accessCount == 1){
            readingThreads.remove(callingThread);
        } else {
            readingThreads.put(callingThread, (accessCount - 1));
        }
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        writeRequests++;
        Thread callingThread = Thread.currentThread();
        while (!canGrantWriteAccess(callingThread)){
            wait();
        }
        writeRequests--;
        writeAccesses++;
        wringThread = callingThread;
    }

    public synchronized void unlockWrite(){
       if (!isWriter(Thread.currentThread())){
           throw new IllegalMonitorStateException(
                   "Calling Thread does not" +
                           "hold the write lock on this ReadWriteLock"
           );
       }
       writeAccesses--;
       if (writeAccesses == 0){
           wringThread = null;
       }
       notifyAll();
    }

    private boolean canGrantReadAccess(Thread callingThread){
       if (isWriter(callingThread)){
           return false;
       }
       if (hasWriter()){
           return false;
       }
       if (isReader(callingThread)){
           return true;
       }
       if (hasWriteRequests()){
           return false;
       }
        return true;
    }

    private boolean canGrantWriteAccess(Thread callingThread){
        if (hasReaders()){
            return false;
        }
        if (isOnlyReader(callingThread)){
            return true;
        }
        if (wringThread == null){
            return true;
        }
        if (!isWriter(callingThread)){
            return false;
        }
        return true;
    }

    private int getReadAccessCount(Thread callingThread){
        Integer accessCount = readingThreads.get(callingThread);
        if (accessCount == null){
            return 0;
        }
        return accessCount.intValue();
    }

    private boolean isReader(Thread callingThread){
        return readingThreads.get(callingThread) != null;
    }
    private boolean isOnlyReader(Thread callingThread){
        return readingThreads.size() == 1 && readingThreads.get(callingThread) != null;
    }
    private boolean isWriter(Thread callingThread){
        return wringThread == callingThread;
    }
    private boolean hasReaders(){
        return readingThreads.size() > 0;
    }
    private boolean hasWriter(){
        return wringThread != null;
    }
    private boolean hasWriteRequests(){
        return this.writeRequests > 0;
    }
}
