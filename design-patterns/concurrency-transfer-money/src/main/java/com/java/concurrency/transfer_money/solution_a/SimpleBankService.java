package com.java.concurrency.transfer_money.solution_a;

import java.math.BigDecimal;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleBankService implements BankService {
  private final Lock lock;
  private final Condition availableFund;

  public SimpleBankService() {
    lock = new ReentrantLock();
    availableFund = lock.newCondition();
  }

  @Override
  public void transfer(Account from, Account to, BigDecimal amount) {
    try {
      lock.lock();

      while (amount.compareTo(from.getBalance()) > 1) availableFund.await();

      from.withdraw(amount);
      to.deposit(amount);

      availableFund.signalAll();

    } catch (Throwable ex) {
      throw new RuntimeException(ex.getMessage(), ex);
    } finally {
      lock.unlock();
    }
  }
}
