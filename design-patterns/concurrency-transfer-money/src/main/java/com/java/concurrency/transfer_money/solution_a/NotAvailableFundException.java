package com.java.concurrency.transfer_money.solution_a;

public class NotAvailableFundException extends RuntimeException {
  public static final RuntimeException INSTANCE =
      new RuntimeException("There is not available fund.");

  private NotAvailableFundException() {}
}
