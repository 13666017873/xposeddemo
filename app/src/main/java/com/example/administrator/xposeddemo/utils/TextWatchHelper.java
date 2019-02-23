package com.example.administrator.xposeddemo.utils;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextWatchHelper implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public abstract void onTextChanged(CharSequence charSequence, int i, int i1, int i2);

    @Override
    public  void afterTextChanged(Editable editable){

    }
}
