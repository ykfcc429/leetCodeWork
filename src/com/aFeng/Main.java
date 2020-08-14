
package com.aFeng;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;


/**
 * 一个简单的redis分布式锁
 * 对比单机的优势在于,在多个消费者的请求下,能够保证redis里的数据恒统一
 * 缺点是资源从排队变成了竞争,想要获得资源必须不断的请求,排队就无效了,谁能请求到就处理谁,这样对于用户来说是非常操蛋的
 * 所以为什么集群中必须需要消息队列的,还是需要排队的,只是对比于单机应用,我们不再用synchronized来对线程排队,而是使用消息队列对请求进行排队
 * 把队列放在了消费者集群的门口,这样就能保证用户只需发起单次请求,就能被处理到,而不是直接被分布式锁丢弃请求.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
//        Lock lock = new Lock();
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                Jedis jedis = new Jedis();
                jedis.lpush("goodsList",Thread.currentThread().getName());
                jedis.close();
            }).start();
        }
    }

}

class HandleMq implements Runnable{
    @Override
    public void run() {

    }
}