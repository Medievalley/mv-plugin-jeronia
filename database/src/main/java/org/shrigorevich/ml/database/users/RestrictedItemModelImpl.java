package org.shrigorevich.ml.database.users;

import org.shrigorevich.ml.state.users.RestrictedItemModel;

public class RestrictedItemModelImpl implements RestrictedItemModel {

    private String type;
    private String jobAllowances;
    private String roleAllowances;
    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getJobAllowances() {
        return jobAllowances;
    }

    @Override
    public String getRoleAllowances() {
        return roleAllowances;
    }


    public void setType(String type) {
        this.type = type;
    }


    public void setJobAllowances(String jobAllowances) {
        this.jobAllowances = jobAllowances;
    }


    public void setRoleAllowances(String roleAllowances) {
        this.roleAllowances = roleAllowances;
    }
}
