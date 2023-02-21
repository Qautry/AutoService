package com.zlj.autoservice;

import android.util.Log;

import com.service.annotations.AutoService;


/**
 * @author dengxiaoqiu
 */
@AutoService(MyServiceImpl.class)
public class MyServiceImplOne implements MyServiceImpl {
    @Override
    public boolean execute(String data, int cmd) {
        Log.e("MyServiceImplOne", "execute");

        return false;
    }
}
