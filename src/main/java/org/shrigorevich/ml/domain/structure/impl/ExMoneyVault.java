package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.MoneyVault;

public interface ExMoneyVault extends MoneyVault {
    void updateDeposit(int amount);
}
