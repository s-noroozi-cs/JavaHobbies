package com.java.design_pattern.concurrency.producer_consumer;


import java.util.LinkedList;
import java.util.Queue;

public class SolutionC {
  static final String[] messages = {
    "Hi!!", "How are you!!", "I love you!", "What's going on?!!", "That's really funny!!"
  };

  static class Data {
    final Queue<String> queue = new LinkedList<>();
    final int capacity;

    public Data(int capacity) {
      this.capacity = capacity;
    }

    public synchronized void publish(String msg) throws InterruptedException {
      String name = Thread.currentThread().getName();
      if (queue.size() == capacity) {
        System.out.println("Queue Full!" + name + " waiting for message to be consumed...");
        wait();
      }
      queue.add(msg);
      System.out.println("Message published:: " + msg);
      System.out.println("Queue: " + queue);
      System.out.println();
      notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
      String name = Thread.currentThread().getName();
      if (queue.size() == 0) {
        System.out.println(name + " waiting for new message...");
        wait();
      }
      String msg = queue.poll();
      System.out.println(name + " has consumed msg:: " + msg);
      System.out.println("Queue: " + queue);
      System.out.println();
      notifyAll();
    }
  }

  static class Producer implements Runnable {
    final Data data;

    public Producer(Data data) {
      this.data = data;
    }

    @Override
    public void run() {
      int i = 0;
      try {
        while (true) {
          Thread.sleep(1000);
          data.publish(messages[i]);
          i = (i + 1) % messages.length;
        }
      } catch (InterruptedException e) {
      }
    }
  }

  static class Consumer implements Runnable {
    final Data data;

    public Consumer(Data data) {
      this.data = data;
    }

    @Override
    public void run() {
      try {
        while (true) {
          Thread.sleep(2000);
          data.consume();
        }
      } catch (InterruptedException e) {
      }
    }
  }

  public static void main(String[] args) {
    Data data = new Data(5);
    Thread producer = new Thread(new Producer(data), "producer");
    Thread consumer = new Thread(new Consumer(data), "consumer");
    producer.start();
    consumer.start();
  }
}
