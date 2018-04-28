package com.zxk.hz.autoverticalscrollview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zxk.hz.library.AutoVerticalScrollView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TestAdapter testAdapter;
    AutoVerticalScrollView autoVerticalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoVerticalScrollView = findViewById(R.id.autoverticalScrollView);
        autoVerticalScrollView.setLimit(3);
        testAdapter = new TestAdapter(this);
        autoVerticalScrollView.setAdapter(testAdapter);
        initData();
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("hello world" + i);
        }
        testAdapter.refreshUIByReplaceData(list);
        autoVerticalScrollView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoVerticalScrollView.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoVerticalScrollView.stop();
    }
}
