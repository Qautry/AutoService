package com.zlj.autoservice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author dengxiaoqiu
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        findViewById (R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceLoader<MyServiceImpl> serviceLoader = ServiceLoader.load(MyServiceImpl.class);
                Iterator<MyServiceImpl> iterator = serviceLoader.iterator();
                while (iterator.hasNext()){
                    MyServiceImpl next = iterator.next();
                    next.execute("1231",12312);
                }
            }
        });

    }
}