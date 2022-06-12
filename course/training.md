# Training Hazelcast Developer Essentials

## Configuring Hazelcast

### Deploy

embedded：edge compute，microservice

client-server：TCP/IP 

### Cluster Config

程序配置，嵌入在Java里面，其他语言暂时只能用xml，yaml配置。

hz分区数也可以改变，默认**271**。

Rolling（数据会不一致） Restart/ShutDown（数据会丢）/Restart，

### Operation

License Key

Discovery（Form，Find，Join）

- TCP/IP Multicast（test/poc 不适合生产）
- TCP/IP Unicast （生产用这个单播更好，why？至少2个peer address）

Partition Group，256，123，123 【128，128是一个group，256是一个group】

Cluster Name 资源隔离，一台机器，可以运行多个集群。DEV/PROD

### Data Structure 

公共配置：backup-count=1，async-backup-count=0

通配符 * 可以创建模板templates。name=Queue10K.*  

动态增加backup-count。是否支持？



## Distributed Concurrency

Mostly AP

CP Subsystem: linearizability   jepsen pass

CAP:c(provide latest data or an error),a(always provide data),p(keep operating in case of failure)

CP Persitent 是企业版特性。

IAtomicLong，a节点1+2，b节点1+3，结果是什么？原子性必须使用IFunction.

Best Practice：Send Code To Data. 

分布式并发数据结构：ICountDownLatch，ISemaphore，IAtomicLong，IAtomicReference，但是没有ILock，而是FenceLock.

信号量实现比锁要简单？

Fence：add a token to the lock service and prevent out-of-order access due to precess delays
参考：chubby lock，https://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html

FencedLock：Linearizable，distributed，reentrant lock，reliable


## Introduction to Hazelcast Platform 5.0

https://training.hazelcast.com/path/hazelcast-developer-essentials-50/introduction-to-hazelcast-platform-50/925705/scorm/1yvmk2gl964pm

Data at Rest/Data at Motion

In-memory storage/computing/Real processing

benchmark工具：https://github.com/nexmark/nexmark

High Density Memory Store(高密度存储)

Primary/Backup

271个partion。

Synchronous Backups(Blocked)/Asynchronous Backups

HZ大部分不是分区的。（分区：蛋糕  不分区：猫）

k/v pair叫entry。

IMap:partition，backup，concurrent access

IMCG:Distributed Query, Entry Processor, Executor Service

IM Computing(Local Data) VS Real Time Processing(Source,Sink)


## In-Memory Storage Essentials

Expiration（多久没访问就删除）/Eviction（内存超过之后策略）

IMap：read through，write through，write behind

persistence：企业版特性

IMap是kv store.

IMap put序列化 get反序列化。

Expiration(Time)：remove unused entries after a configured amount of time. 单个map或某个key

Eviction(Consumption)：remove entries when memory use reaches a config limit. LFU，LRU，Custom，PER_NODE,PER_PARTITION

evictAll移除所有未锁定的map

Adhoc或者federate查询(IMap和CSV join查询)

SQL Mapping可以是CSV/JSON，也可以是IMap，也可以是Kafka+Avro

HDMS:高密度内存存储

IMap Query：SqlPredicate，PredicateBuilder，PagePredicate

Aggregator Interface.

有点像内存数据库。支持SQL，分页，排序，sum，min/max，average等操作。可以进行很多计算操作。

Predicate和SQL Engine Query区别？

Projection操作（transform）

提高查询性能，OBJECT，Indexing，Projection，Query Limit Size

IMDG：特指以及大部分特性实现在IMap上面。get/put/"query"

访问持久化访问模式：通读(同步读)，通写(同步写)，后写(异步写)。get/load put/store，没有了cache-aside模式

evict不会删除持久化数据，delete，remove会删除。

MapLoader/MapStore 必须是java。

Access Cassnadra，MongoDB

Replicated Map:read/write to any member,eventual consistency（background process），relatively，immutable。

HDMS：数据不在heap存了。企业级特性。    持久内存：Intel Optane。

- In-Memory Format
  - BINARY(map put/get, k v is binary)
  - NATIVE（HDMS格式）
  - OBJECT(map query/entry processing, k is binary,v is object，减少了反序列化的消耗，in-memory compute)

map的操作有：put get entry-processing，query

Queue是阻塞队列。Listener是事件驱动。

ReliableTopic基于RingBuffer，用于backup，比起Topic有backup，Topic消息可能会丢失。

RingBuffer  固定，循环的数组。head和tail总在变。超过capacity后，tail，head都会变。

IQueue：Distributed Queue，FIFO

Topic：Distributed Message

RingBuffer：Distributed ”looping“ Array (性能比IQueue要好，ReliableTopic用于重发消息，可以通过消息id找到消息)


## In-Memory Computing Essentials

### Entry Processor

full compute，not just read/write

AbstractEntryProcessor 是便利实现。原子批量操作。分布式内存处理。

ReadOnly EntryProcessor/Mutate EntryProcessor/Offloadable Processor（是否类似于HBase Coprocessor）？

### Entry Listener

hz 事件框架。 IMap，IQueue，Member

Map Listener，MapInterceptor（修改put的值，或者get的值）

Map的计算（Data Change）：

- **Before**：MapInterceptor（to stop or adjust an entry change）
- **During**：EntryProcessor（change entry in-situ）
- **After**：EntryListener/EntryEvent（react to entry change immediately after）



### Push Code to Cluster

- EntryProcessor

- Executor（Runnable，Callable） 并行分布式处理，任意代码

  - BaseExecutor

  - DurableExecutor
  - ScheduleExecutor

### Map Lock

”Lock Safe“ Threading

map lock  map unlock   悲观锁

map replace 乐观锁（新加version避免ABA问题）



## Stream Processing Essentials

https://github.com/hazelcast/hazelcast-jet-training.git

Pipeline/Source/Sink

流处理引擎叫Jet，可以处理批和流。流处理引擎将pipleline转成job。

Pipeline使用声明式编程。处理How的问题。将declarative转换成DAG。

分布式并行处理。

Word Count：Source/Tokenize/Aggregate/Sink 

Transcation Falut Tolerance（Exactly Once）

- Replayable Source -》JET-》Idempotent Sink

- Distrubted Transaction (Acknowledagment Source/Transactional Sink)  2PC：P1：prepare P2：commit
