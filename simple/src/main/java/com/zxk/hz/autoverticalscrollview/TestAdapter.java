package com.zxk.hz.autoverticalscrollview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.zxk.hz.library.AutoVerticalScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * 　　┏┓　　　　┏┓
 * 　┏┛┻━━━━┛┻┓
 * 　┃　　　　　　　　┃
 * 　┃　　　━　　　　┃
 * 　┃　┳┛　┗┳　　┃
 * 　┃　　　　　　　　┃
 * 　┃　　　┻　　　　┃
 * 　┃　　　　　　　　┃
 * 　┗━━┓　　　┏━┛
 * 　　　　┃　　　┃　　　神兽保佑
 * 　　　　┃　　　┃　　　代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * <p>
 * Created by zxk on 18-4-24.
 */

public class TestAdapter extends AutoVerticalScrollView.Adapter {
    private List<String> mDataList;
    private Context mContext;
    private OnItemClick mOnItemClick;

    public TestAdapter(Context context) {
        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    public TestAdapter(Context context, List<String> list) {
        this.mDataList = list;
        this.mContext = context;
    }

    public void refreshUIByReplaceData(List<String> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void refreshUIByAddData(List<String> list) {
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }


    public interface OnItemClick {
        void onClick(String commonTopicBean);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    @Override
    public View getView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_scroll, null);
        TextView text = view.findViewById(R.id.text);
        final String str = mDataList.get(position);
        if (!TextUtils.isEmpty(str)) {
            text.setText(str);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClick != null) {
                        mOnItemClick.onClick(str);
                    }
                }
            });
        }

        return view;
    }

    @Override
    public int itemCount() {
        return mDataList.size();
    }


}
