package com.java.design_pattern.concurrency.producer_consumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SolutionFinal {
  BlockingQueue<String> q = new ArrayBlockingQueue<>(10);

  static class Producer implements Runnable {
    BlockingQueue<String> q;
    final String[] messages = {
      "Hi!!", "How are you!!", "I love you!", "What's going on?!!", "That's really funny!!"
    };

    public Producer(BlockingQueue<String> q) {
      this.q = q;
    }

    @Override
    public void run() {
      int i = 0;
      try {
        while (true) {
          Thread.sleep(500);

          q.put(messages[i]);

          System.out.println("Message published:: " + messages[i]);
          i = (i + 1) % messages.length;
        }
      } catch (InterruptedException e) {
      }
    }
  }

  static class Consumer implements Runnable {
    BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q) {
      this.q = q;
    }

    @Override
    public void run() {
      try {
        while (true) {
          Thread.sleep(1500);

          String msg = q.take();

          String name = Thread.currentThread().getName();
          System.out.println(name + " has consumed msg:: " + msg);
        }
      } catch (InterruptedException e) {
      }
    }
  }

  public static void main(String[] args) {
    BlockingQueue<String> q = new ArrayBlockingQueue<>(10);
    Thread producer = new Thread(new Producer(q));
    producer.start();
    for (int i = 1; i <= 5; i++) new Thread(new Consumer(q), "Consumer " + i).start();
  }
}
