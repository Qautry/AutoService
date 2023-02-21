package com.zlj.autoservice;


import android.util.Log;

import com.service.annotations.AutoService;


/**
 * @author dengxiaoqiu
 */
@AutoService(MyServiceImpl.class)
public class MyServiceImplTwo implements MyServiceImpl {
    @Override
    public boolean execute(String data, int cmd) {
        Log.e("MyServiceImplTwo", "execute");
        return false;
    }

    @AutoService(MyServiceImpl.class)
    public static class MyServiceImplThree implements MyServiceImpl {

        @Override
        public boolean execute(String data, int cmd) {
            Log.e("MyServiceImplThree", "execute");
            return false;
        }
    }
}
