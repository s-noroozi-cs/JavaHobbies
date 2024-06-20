package com.java.concurrency.transfer_money.solution_a;

import java.math.BigDecimal;

public class SimpleAccount implements Account {
  private BigDecimal balance;

  public SimpleAccount(BigDecimal balance) {
    this.balance = balance;
  }

  @Override
  public BigDecimal getBalance() {
    return balance;
  }

  @Override
  public BigDecimal withdraw(BigDecimal amount) {
    if (balance.compareTo(amount) >= 0) balance = balance.subtract(amount);
    else throw NotAvailableFundException.INSTANCE;
    return balance;
  }

  @Override
  public BigDecimal deposit(BigDecimal amount) {
    balance = balance.add(amount);
    return balance;
  }
}
