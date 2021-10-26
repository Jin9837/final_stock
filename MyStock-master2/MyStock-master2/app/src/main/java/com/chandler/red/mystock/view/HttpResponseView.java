package com.chandler.red.mystock.view;

public interface HttpResponseView<T> extends BaseView{
    void onSuccess(T responseBody);
    void onError(String result);
}
