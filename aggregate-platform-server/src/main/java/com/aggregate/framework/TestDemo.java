package com.aggregate.framework;

import java.util.concurrent.*;

public class TestDemo {
    public void sendRequest(){

        int advertCount = 5;
        CountDownLatch countDownLatch = new CountDownLatch(1);



        for (int i = 0; i < advertCount; i ++) {
            new Thread(new Worker(countDownLatch)).start();
        }


        // 启动多个线程
        countDownLatch.countDown();


    }

    class Worker implements  Runnable{

        private CountDownLatch cd;

        public Worker(CountDownLatch cd){
            this.cd = cd;
        }

        @Override
        public void run() {
            try {
                // 线程等待
                cd.await();

                // 执行操作
                String str = HttpUtil.httpGet("http://localhost:8080/open/api/test");
                System.out.println("return str ; " + str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TestDemo testDemo = new TestDemo();
        testDemo.sendRequest();
    }

}
