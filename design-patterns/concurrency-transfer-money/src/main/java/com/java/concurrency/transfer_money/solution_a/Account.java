package com.java.concurrency.transfer_money.solution_a;

import java.math.BigDecimal;

public interface Account {
    BigDecimal getBalance();
    BigDecimal withdraw(BigDecimal amount);
    BigDecimal deposit(BigDecimal amount);
}
