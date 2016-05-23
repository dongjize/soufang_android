package com.dong.soufang.custom.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Description: 可缩放平移的图片控件
 * <p>
 * Author: dong
 * Date: 16/5/5
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private boolean once = false;

    //初始化时缩放的值
    private float mInitScale;
    //双击放大时到达的值
    private float mMinScale;
    //放大的最大值
    private float mMaxScale;

    private Matrix mScaleMatrix;

    //捕获用户多指触控时缩放的比例
    private ScaleGestureDetector detector;

    //------自由移动------
    //记录上一次多点触控的数量
    private int mLastPointerCount;
    //上一状态的x,y坐标
    private float mLastX, mLastY;
    //
    private int mTouchSlop;
    //是否可以拖曳
    private boolean canDrag;

    private boolean isCheckLeftRight;
    private boolean isCheckTopBottom;

    //--------双击放大与缩小--------
    //手势感应器
    private GestureDetector mGestureDetector;

    private boolean isAutoScale;


    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX); //与xml中scaleType意义相同

        detector = new ScaleGestureDetector(context, this);

        setOnTouchListener(this);

        /*getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
         如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页*/
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (isAutoScale) {
                            return true;
                        }
                        float x = e.getX();
                        float y = e.getY();
                        if (getScale() < mMinScale) {
//                            mScaleMatrix.postScale(mMinScale / getScale(), mMinScale / getScale(), x, y);
//                            setImageMatrix(mScaleMatrix);
                            postDelayed(new AutoScaleRunnable(mMinScale, x, y), 16);
                            isAutoScale = true;
                        } else {
//                            mScaleMatrix.postScale(mInitScale / getScale(), mInitScale / getScale(), x, y);
//                            setImageMatrix(mScaleMatrix);
                            postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                            isAutoScale = true;
                        }
                        return true;
                    }
                });
    }

    /**
     * 内部类:自动放大与缩小
     */
    private class AutoScaleRunnable implements Runnable {
        float targetScale; //缩放目标值
        private float x, y;
        private final float BIGGER = 1.07f; //放大梯度
        private final float SMALL = 0.93f; //缩小梯度

        private float tmpScale; //

        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.targetScale = targetScale;
            this.x = x;
            this.y = y;

            if (getScale() < targetScale) {
                tmpScale = BIGGER;
            }
            if (getScale() > targetScale) {
                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            //进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenter();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();
            //如果还没有放大或缩小到临界值
            if ((tmpScale > 1.0f && currentScale < targetScale)
                    || (tmpScale < 1.0f && currentScale > targetScale)) {
                postDelayed(this, 16);
            } else { //设置为目标值
                float scale = targetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenter();
                setImageMatrix(mScaleMatrix);

                isAutoScale = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this); //最低api16
        //getViewTreeObserver().removeGlobalOnLayoutListener(this); //过时
    }

    /**
     * 获取imageview加载的图片
     */
    @Override
    public void onGlobalLayout() {
        if (!once) {
            //得到父控件的宽和高
            int width = getWidth();
            int height = getHeight();

            //得到图片以及宽和高
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            //进行图片与控件的尺寸对比
            float scale = 1.0f;
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }
            if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            }
            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            //得到初始化时缩放的比例
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMinScale = mInitScale * 2;

            //将图片移动到当前控件(屏幕)的中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;
            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);

            once = true;
        }
    }

    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //当前相对于初始的缩放值
        float scale = getScale();
        //相对于上个瞬间缩放值
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return true;
        }

        //缩放范围的控制
        //如果正在放大且未达到极限,或正在缩小且未达到极限
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            //缩放
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

            checkBorderAndCenter();

            setImageMatrix(mScaleMatrix);
        }

        return true;
    }

    /**
     * 获得图片放大缩小以后的宽高,以及l,r,t,b
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 在缩放的时候进行边界控制和位置控制
     */
    private void checkBorderAndCenter() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();

        //缩放时进行边界检测,防止图片出现白边
        if (rect.width() >= width) {
            //如果左边出现空隙,图片向左移动
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            //右边出现空隙,图片向右移动
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        //如果宽度或高度小于屏幕尺寸,居中
        if (rect.width() < width) {
            deltaX = width / 2f - rect.right + rect.width() / 2f;
        }
        if (rect.height() < height) {
            deltaY = height / 2f - rect.bottom + rect.height() / 2f;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        detector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;
        if (mLastPointerCount != pointerCount) {
            canDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;

        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    //请求不允许父控件拦截事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + 0.01 || rectF.height() > getHeight() + 0.01) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!canDrag) {
                    canDrag = isMoveAction(dx, dy);
                }
                if (canDrag) {
                    if (getDrawable() != null) {
                        //默认可以检测
                        isCheckLeftRight = isCheckTopBottom = true;
                        //如果宽度小于控件,不允许移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftRight = false;
                            dx = 0;
                        }
                        //如果高度小于控件,不允许移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopBottom = false;
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);

                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }

                if (isMoveAction(dx, dy)) {
                    canDrag = true;
                }
                //实时更新坐标
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
                mLastPointerCount = 0;
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * 移动的时候检测边界,限制移动范围
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCheckTopBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < height && isCheckTopBottom) {
            deltaY = height - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckLeftRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && isCheckLeftRight) {
            deltaX = width - rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 判断是否是move
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}
