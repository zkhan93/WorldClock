package anonestep.com.worldclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Madhur Jain on 4/26/2017.
 */

public class AnalogClock extends View {

    private static final String TAG = AnalogClock.class.getSimpleName();
    private Calendar mTime;
    private String mTimeZone;
    Context mContext;
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mDial;

    private int mDialWidth;
    private int mDialHeight;

    private boolean mAttached;

    private float mMinutes;
    private float mHour;
    private boolean mChanged;

    public AnalogClock(Context context) {
        this(context, null);
    }

    public AnalogClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AnalogClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final Resources resources = context.getResources();
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.AnalogClock, defStyleAttr, defStyleRes);

        mDial = context.getDrawable(R.drawable.clock_dial);

        mHourHand = context.getDrawable(R.drawable.clock_hand_hour);

        mMinuteHand = context.getDrawable(R.drawable.clock_hand_minute);

        mTime = Calendar.getInstance();
        Log.d("HAI1", mTime.getTimeZone().getID());
        setTimeZone(mTime.getTimeZone().getID());
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();
            mContext = getContext();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            // OK, this is gross but needed. This class is supported by the
            // remote views machanism and as a part of that the remote views
            // can be inflated by a context for another user without the app
            // having interact users permission - just for loading resources.
            // For exmaple, when adding widgets from a user profile to the
            // home screen. Therefore, we register the receiver as the current
            // user not the one the context is for.
            mContext.registerReceiver(mIntentReceiver, filter, null, getHandler());
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        Log.d("HAI2", mTime.getTimeZone().getID());
        // Make sure we update to the current time
        onTimeChanged();
    }


    public void setTimeZone(String timeZone) {
        if (timeZone != null) {
            this.mTimeZone = timeZone;
            mTime.setTimeZone(TimeZone.getTimeZone(timeZone));
            Log.d("HAI3", mTime.getTimeZone().getID());

        } else {
            mTime = Calendar.getInstance();
            Log.d("HAI4", mTime.getTimeZone().getID());
        }
        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        int availableWidth = getWidth();
        int availableHeight = getHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();

        if (scaled) {
            canvas.restore();
        }
    }

    private void onTimeChanged() {
        //mTime.setTime(new Date());

        //mCalendar.setToNow();
        int hour = mTime.get(Calendar.HOUR);
        int minute = mTime.get(Calendar.MINUTE);
        int second = mTime.get(Calendar.SECOND);
        Log.d(TAG, hour + ":" + minute + ":" + second + "" + mTime.getTimeZone().getDisplayName());
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;

        updateContentDescription(mTime);
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mTimeZone == null && intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mTime = Calendar.getInstance();
                mTime.setTimeZone(TimeZone.getTimeZone(tz));
                Log.d("HAI6", mTime.getTimeZone().getID());
            } else {
                mTime = Calendar.getInstance();
                mTime.setTimeZone(TimeZone.getTimeZone(mTimeZone));
                Log.d("HAI7", mTime.get(Calendar.HOUR) + " " + mTime.get(Calendar.MINUTE) + " ");
            }
            onTimeChanged();
            invalidate();
        }
    };

    private void updateContentDescription(Calendar calendar) {
        final int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;
        calendar.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Log.d("HAI8", calendar.getTimeZone().getID() + " " + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.SECOND));
        String contentDescription = DateUtils.formatDateTime(mContext, calendar.getTimeInMillis(), flags);
        setContentDescription(contentDescription);
    }

}
