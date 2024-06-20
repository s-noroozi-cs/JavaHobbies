package com.java.concurrency.transfer_money.solution_a;

import java.math.BigDecimal;

public interface BankService {
    void transfer(Account from, Account to, BigDecimal amount);
}
