package com.example.panggung.base;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
