package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.domain.structures.MoneyVault;

public interface ExMoneyVault extends MoneyVault {
    void updateDeposit(int amount);
}
