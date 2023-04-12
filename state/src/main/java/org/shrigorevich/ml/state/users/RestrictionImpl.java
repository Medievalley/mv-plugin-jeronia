package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.User;

import java.util.List;

public class RestrictionImpl implements Restriction{
    private List<? extends Allowance> allowances;

    public RestrictionImpl(List<Allowance> allowances){
        this.allowances = allowances;
    };
    @Override
    public boolean isAllowed(User user) {
        return false;
    }
}
