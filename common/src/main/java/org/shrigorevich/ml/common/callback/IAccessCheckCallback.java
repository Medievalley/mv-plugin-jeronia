package org.shrigorevich.ml.common.callback;

public interface IAccessCheckCallback {

    void onCheck(boolean isAllowed, String msg);
}
