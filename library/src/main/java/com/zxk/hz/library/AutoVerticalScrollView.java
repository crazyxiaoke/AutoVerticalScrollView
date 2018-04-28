package com.zxk.hz.library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Observable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import java.util.Timer;
import java.util.TimerTask;
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
 * Created by zxk on 18-4-23.
 */

public class AutoVerticalScrollView extends ScrollView {
    private static final int MSG_AUTO_SCROLL = 1;
    private final AdapterDataObserver mObserver = new AdapterDataObserver();
    private Adapter mAdapter;
    private int mLimit = 2;  //最多显示几行
    private int mChildHeight = 0;  //每个Item的高度
    private int mCurrentIndex = -1; //下一个滚动的item的index

    private Timer mTimer;
    private TimerTask mTimerTask;

    private LinearLayout mParentLayout;

    private boolean mStop = true;

    private boolean mIsAutoScroll = true;

    public AutoVerticalScrollView(Context context) {
        super(context);
        init(context);
    }

    public AutoVerticalScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoVerticalScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mChildHeight = getMeasuredHeight() / mLimit;
        Log.e("TAG", "mChildHeight=" + mChildHeight);
        mParentLayout = new LinearLayout(context);
        mParentLayout.setBackgroundColor(0xffacbdca);
        mParentLayout.setOrientation(LinearLayout.VERTICAL);
        mParentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mParentLayout);
    }

    private void setView() {
        if (mAdapter != null) {
            Log.e("TAG", "mAdapter.count=" + mAdapter.itemCount());
            mParentLayout.removeAllViews();
            int count = mAdapter.itemCount();
            if (count > mLimit) {
                mIsAutoScroll = true; //如果总数大于limt数，开启自动滚动
                for (int i = 0; i < mLimit; i++) {
                    View v1;
                    if (mCurrentIndex == -1) {  //如果mCurrentIndex=-1,初始化
                        v1 = mAdapter.getView(i);
                    } else {
                        //根据上次保存的index，获取前limt条数据，保证view不会跳动
                        v1 = mAdapter.getView((mCurrentIndex - (mLimit - i)) % count);
                    }
                    if (v1 != null) {
                        mParentLayout.addView(v1);
                    }
                }
//                start();
            } else {
                //如果总数小于limt数，关闭自动滚动
                mIsAutoScroll = false;
                for (int i = 0; i < count; i++) {
                    View v1 = mAdapter.getView(i);
                    mParentLayout.addView(v1);
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTO_SCROLL:
                    if (mAdapter != null && mAdapter.itemCount() > 0) {
                        if (mCurrentIndex == -1) {
                            //设置初始滚动index
                            mCurrentIndex = mLimit;
                        }
                        View v = mAdapter.getView(mCurrentIndex % mAdapter.itemCount());
                        mParentLayout.addView(v);
                        //记录下一条滚动index
                        mCurrentIndex++;
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                //获取滚动条高度
                                int offset = mParentLayout.getMeasuredHeight() - getHeight();
                                if (offset < 0) {
                                    offset = 0;
                                }
                                //启动动画
                                setAnimation(offset);
                            }
                        });

                    }
                    break;
            }
            return false;
        }
    });


    public void start() {
        mStop = false;
        if (mIsAutoScroll) {
            if (mTimer == null) {
                mTimer = new Timer();
            }
            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_AUTO_SCROLL);
                    }
                };
            }
            mTimer.schedule(mTimerTask, 3000, 3000);
        }
    }

    public void stop() {
        mStop = true;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public boolean isStop() {
        return mStop;
    }

    private void setAnimation(int offset) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, offset);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                scrollTo(0, value);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mParentLayout.removeViewAt(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
        }
        this.mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(mObserver);
        }
        setView();
    }

    public void setLimit(int limit) {
        mLimit = limit;
    }

    public abstract static class Adapter {
        private AdapterDataObservable mObservable = new AdapterDataObservable();

        public abstract View getView(int position);

        public abstract int itemCount();

        public void notifyDataSetChanged() {
            mObservable.notifyChange();
        }

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            mObservable.unregisterObserver(observer);
        }
    }


    static class AdapterDataObservable extends Observable<AdapterDataObserver> {

        public void notifyChange() {
            if (!mObservers.isEmpty()) {
                for (int i = mObservers.size() - 1; i >= 0; i--) {
                    mObservers.get(i).onChange();
                }
            }
        }
    }

    private class AdapterDataObserver {
        public void onChange() {
            //Do nothing
            setView();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
