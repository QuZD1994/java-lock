# java--lock
java锁的相关知识
锁像 synchronized 同步块一样，是一种线程同步机制，但比 Java 中的 synchronized 同步块更复杂。因为锁（以及其它更高级的线程同步机制）是由 synchronized 同步块的方式实现的，所以我们还不能完全摆脱 synchronized 关键字（译者注：这说的是 Java 5 之前的情况）。

自 Java 5 开始，java.util.concurrent.locks 包中包含了一些锁的实现，因此你不用去实现自己的锁了。但是你仍然需要去了解怎样使用这些锁，且了解这些实现背后的理论也是很有用处的。可以参考我对 java.util.concurrent.locks.Lock 的介绍，以了解更多关于锁的信息。
