package com.dd;

import com.dd.circular.progress.button.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.widget.Button;

public class CircularProgressButton extends Button {

    private StrokeGradientDrawable background;
    private CircularAnimatedDrawable mAnimatedDrawable;
    private CircularProgressDrawable mProgressDrawable;

    private ColorStateList mInstallColorState;
    private ColorStateList mOpenColorState;
    private ColorStateList mUpdateColorState;

    private StateListDrawable mInstallStateDrawable;
    private StateListDrawable mOpenStateDrawable;
    private StateListDrawable mUpdateStateDrawable;

    private State mState;
    private State mNewState;
    private String mDownloadText;
    private String mInstallText;
    private String mOpenText;
    private String mUpdateText;
    private String mProgressText;

    private int mColorProgress;
    private int mColorIndicator;
    private int mColorIndicatorBackground;
    private int mIconComplete;
    private int mIconError;
    private int mStrokeWidth;
    private int mPaddingProgress;
    private float mCornerRadius;
    private boolean mIndeterminateProgressMode;
    private boolean mConfigurationChanged;

    public enum State {
        PROGRESS, DOWNLOAD, INSTALL, OPEN, UPDATE
    }

    private int mMaxProgress;

    private boolean mMorphingInProgress;

    public CircularProgressButton(Context context) {
        super(context);
        init(context, null);
    }

    public CircularProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {

        mStrokeWidth = (int) getContext().getResources().getDimension(R.dimen.cpb_stroke_width);

        initAttributes(context, attributeSet);

        mMaxProgress = 100;
        mState = State.INSTALL;

    }

    private void initUpdateStateDrawable() {

        int colorPressed = getPressedColor(mUpdateColorState);

        StrokeGradientDrawable drawablePressed = createDrawable(colorPressed);
        mUpdateStateDrawable = new StateListDrawable();

        mUpdateStateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed.getGradientDrawable());
        mUpdateStateDrawable.addState(StateSet.WILD_CARD, background.getGradientDrawable());
    }

    private void initOpenStateDrawable() {

        int colorPressed = getPressedColor(mOpenColorState);

        StrokeGradientDrawable drawablePressed = createDrawable(colorPressed);
        mOpenStateDrawable = new StateListDrawable();

        mOpenStateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed.getGradientDrawable());
        mOpenStateDrawable.addState(StateSet.WILD_CARD, background.getGradientDrawable());
    }

    private void initInstallStateDrawable() {

        int colorNormal = getNormalColor(mInstallColorState);
        int colorPressed = getPressedColor(mInstallColorState);
        int colorFocused = getFocusedColor(mInstallColorState);
        int colorDisabled = getDisabledColor(mInstallColorState);

        if (background == null) {
            background = createDrawable(colorNormal);
        }

        StrokeGradientDrawable drawableDisabled = createDrawable(colorDisabled);
        StrokeGradientDrawable drawableFocused = createDrawable(colorFocused);
        StrokeGradientDrawable drawablePressed = createDrawable(colorPressed);
        mInstallStateDrawable = new StateListDrawable();

        mInstallStateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed.getGradientDrawable());
        mInstallStateDrawable.addState(new int[]{android.R.attr.state_focused}, drawableFocused.getGradientDrawable());
        mInstallStateDrawable.addState(new int[]{-android.R.attr.state_enabled}, drawableDisabled.getGradientDrawable());
        mInstallStateDrawable.addState(StateSet.WILD_CARD, background.getGradientDrawable());
    }

    private void initDownloadStateDrawable() {

        int colorNormal = getNormalColor(mInstallColorState);
        int colorPressed = getPressedColor(mInstallColorState);
        int colorFocused = getFocusedColor(mInstallColorState);
        int colorDisabled = getDisabledColor(mInstallColorState);

        if (background == null) {
            background = createDrawable(colorNormal);
        }

        StrokeGradientDrawable drawableDisabled = createDrawable(colorDisabled);
        StrokeGradientDrawable drawableFocused = createDrawable(colorFocused);
        StrokeGradientDrawable drawablePressed = createDrawable(colorPressed);
        mInstallStateDrawable = new StateListDrawable();

        mInstallStateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed.getGradientDrawable());
        mInstallStateDrawable.addState(new int[]{android.R.attr.state_focused}, drawableFocused.getGradientDrawable());
        mInstallStateDrawable.addState(new int[]{-android.R.attr.state_enabled}, drawableDisabled.getGradientDrawable());
        mInstallStateDrawable.addState(StateSet.WILD_CARD, background.getGradientDrawable());
    }

    private int getNormalColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, 0);
    }

    private int getPressedColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, 0);
    }

    private int getFocusedColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_focused}, 0);
    }

    private int getDisabledColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{-android.R.attr.state_enabled}, 0);
    }

    private StrokeGradientDrawable createDrawable(int color) {

        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.cpb_background).mutate();
        drawable.setColor(color);
        drawable.setCornerRadius(mCornerRadius);
        StrokeGradientDrawable strokeGradientDrawable = new StrokeGradientDrawable(drawable);
        strokeGradientDrawable.setStrokeColor(color);
        strokeGradientDrawable.setStrokeWidth(mStrokeWidth);

        return strokeGradientDrawable;
    }

    @Override
    protected void drawableStateChanged() {

        if (mState == State.OPEN) {
            initOpenStateDrawable();
            setBackgroundCompat(mOpenStateDrawable);
        } else if(mState == State.DOWNLOAD) {
            initDownloadStateDrawable();
            setBackgroundCompat(mInstallStateDrawable);
        } else if (mState == State.INSTALL) {
            initInstallStateDrawable();
            setBackgroundCompat(mInstallStateDrawable);
        } else if (mState == State.UPDATE) {
            initUpdateStateDrawable();
            setBackgroundCompat(mUpdateStateDrawable);
        }

        if (mState != State.PROGRESS) {
            super.drawableStateChanged();
        }
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {

        TypedArray attr = getTypedArray(context, attributeSet, R.styleable.CircularProgressButton);
        if (attr == null) {
            return;
        }

        try {
            mDownloadText = attr.getString(R.styleable.CircularProgressButton_cpb_textDownload);
            mInstallText = attr.getString(R.styleable.CircularProgressButton_cpb_textInstall);
            mOpenText = attr.getString(R.styleable.CircularProgressButton_cpb_textOpen);
            mUpdateText = attr.getString(R.styleable.CircularProgressButton_cpb_textUpdate);
            mProgressText = attr.getString(R.styleable.CircularProgressButton_cpb_textProgress);

            mIconComplete = attr.getResourceId(R.styleable.CircularProgressButton_cpb_iconOpen, 0);
            mIconError = attr.getResourceId(R.styleable.CircularProgressButton_cpb_iconUpdate, 0);
            mCornerRadius = attr.getDimension(R.styleable.CircularProgressButton_cpb_cornerRadius, 0);
            mPaddingProgress = attr.getDimensionPixelSize(R.styleable.CircularProgressButton_cpb_paddingProgress, 0);


            int idleStateSelector = attr.getResourceId(R.styleable.CircularProgressButton_cpb_selectorInstall,
                    R.color.cpb_idle_state_selector);
            mInstallColorState = getResources().getColorStateList(idleStateSelector);

            int completeStateSelector = attr.getResourceId(R.styleable.CircularProgressButton_cpb_selectorOpen,
                    R.color.cpb_complete_state_selector);
            mOpenColorState = getResources().getColorStateList(completeStateSelector);

            int errorStateSelector = attr.getResourceId(R.styleable.CircularProgressButton_cpb_selectorUpdate,
                    R.color.cpb_error_state_selector);
            mUpdateColorState = getResources().getColorStateList(errorStateSelector);

            mColorProgress = attr.getColor(R.styleable.CircularProgressButton_cpb_colorProgress, getColor(R.color.white));
            mColorIndicator = attr.getColor(R.styleable.CircularProgressButton_cpb_colorIndicator, getColor(R.color.gray_button));
            mColorIndicatorBackground =
                    attr.getColor(R.styleable.CircularProgressButton_cpb_colorIndicatorBackground, getColor(R.color.background_color));
        } finally {
            attr.recycle();
        }
    }

    protected int getColor(int id) {

        return getResources().getColor(id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mState == State.PROGRESS && !mMorphingInProgress) {
            if (mIndeterminateProgressMode) {
                drawIndeterminateProgress(canvas);
            } else {
                drawProgress(canvas);
            }
        }
    }

    private void drawIndeterminateProgress(Canvas canvas) {

        if (mAnimatedDrawable == null) {
            int offset = (getWidth() - getHeight()) / 2;
            mAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator, mStrokeWidth);
            int left = offset + mPaddingProgress;
            int right = getWidth() - offset - mPaddingProgress;
            int bottom = getHeight() - mPaddingProgress;
            int top = mPaddingProgress;
            mAnimatedDrawable.setBounds(left, top, right, bottom);
            mAnimatedDrawable.setCallback(this);
            mAnimatedDrawable.start();
        } else {
            mAnimatedDrawable.draw(canvas);
        }
    }

    private void drawProgress(Canvas canvas) {

        if (mProgressDrawable == null) {
            int offset = (getWidth() - getHeight()) / 2;
            int size = getHeight() - mPaddingProgress * 2;
            mProgressDrawable = new CircularProgressDrawable(size, mStrokeWidth, mColorIndicator);
            int left = offset + mPaddingProgress;
            mProgressDrawable.setBounds(left, mPaddingProgress, left, mPaddingProgress);
        }
        float sweepAngle = (360f / mMaxProgress);//* mProgress
        mProgressDrawable.setSweepAngle(sweepAngle);
        mProgressDrawable.draw(canvas);
    }

    public boolean isIndeterminateProgressMode() {

        return mIndeterminateProgressMode;
    }

    public void setIndeterminateProgressMode(boolean indeterminateProgressMode) {

        this.mIndeterminateProgressMode = indeterminateProgressMode;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {

        return who == mAnimatedDrawable || super.verifyDrawable(who);
    }

    private MorphingAnimation createMorphing() {

        mMorphingInProgress = true;

        MorphingAnimation animation = new MorphingAnimation(this, background);
        animation.setFromCornerRadius(mCornerRadius);
        animation.setToCornerRadius(mCornerRadius);

        animation.setFromWidth(getWidth());
        animation.setToWidth(getWidth());

        if (mConfigurationChanged) {
            animation.setDuration(MorphingAnimation.DURATION_INSTANT);
        } else {
            animation.setDuration(MorphingAnimation.DURATION_NORMAL);
        }

        mConfigurationChanged = false;

        return animation;
    }

    private MorphingAnimation createProgressMorphing(float fromCorner, float toCorner, int fromWidth, int toWidth) {

        mMorphingInProgress = true;

        MorphingAnimation animation = new MorphingAnimation(this, background);
        animation.setFromCornerRadius(fromCorner);
        animation.setToCornerRadius(toCorner);

        animation.setPadding(mPaddingProgress);

        animation.setFromWidth(fromWidth);
        animation.setToWidth(toWidth);

        if (mConfigurationChanged) {
            animation.setDuration(MorphingAnimation.DURATION_INSTANT);
        } else {
            animation.setDuration(MorphingAnimation.DURATION_NORMAL);
        }

        mConfigurationChanged = false;

        return animation;
    }

    public void morphToProgress() {

        setWidth(getWidth());
        setText(mProgressText);
        MorphingAnimation animation = createProgressMorphing(mCornerRadius, getHeight(), getWidth(), getHeight());
        animation.setFromColor(getNormalColor(mInstallColorState));
        animation.setToColor(mColorProgress);
        animation.setFromStrokeColor(getNormalColor(mInstallColorState));
        animation.setToStrokeColor(mColorIndicatorBackground);
        animation.setListener(mProgressStateListener);
        animation.start();
    }

    private OnAnimationEndListener mProgressStateListener = new OnAnimationEndListener() {

        @Override
        public void onAnimationEnd() {
            mMorphingInProgress = false;
            mState = State.PROGRESS;
        }
    };

    private void morphProgressToOpen() {

        MorphingAnimation animation = createProgressMorphing(getHeight(), mCornerRadius, getHeight(), getWidth());

        animation.setFromColor(mColorProgress);
        animation.setToColor(getNormalColor(mOpenColorState));

        animation.setFromStrokeColor(mColorIndicator);
        animation.setToStrokeColor(getNormalColor(mOpenColorState));
        animation.setListener(mOpenStateListener);
        animation.start();
    }

    private void morphInstallToOpen() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mInstallColorState));
        animation.setToColor(getNormalColor(mOpenColorState));

        animation.setFromStrokeColor(getNormalColor(mInstallColorState));
        animation.setToStrokeColor(getNormalColor(mOpenColorState));

        animation.setListener(mOpenStateListener);

        animation.start();

    }

    private void morphUpdateToOpen() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mUpdateColorState));
        animation.setToColor(getNormalColor(mOpenColorState));

        animation.setFromStrokeColor(getNormalColor(mUpdateColorState));
        animation.setToStrokeColor(getNormalColor(mOpenColorState));

        animation.setListener(mOpenStateListener);

        animation.start();

    }

    private void morphDownloadToOpen() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mInstallColorState));
        animation.setToColor(getNormalColor(mOpenColorState));

        animation.setFromStrokeColor(getNormalColor(mInstallColorState));
        animation.setToStrokeColor(getNormalColor(mOpenColorState));

        animation.setListener(mOpenStateListener);

        animation.start();
    }



    private OnAnimationEndListener mOpenStateListener = new OnAnimationEndListener() {

        @Override
        public void onAnimationEnd() {
            if (mIconComplete != 0) {
                setText(null);
                setIcon(mIconComplete);
            } else {
                setText(mOpenText);
            }
            mMorphingInProgress = false;
            mState = State.OPEN;
        }
    };

    public void morphOpenToInstall() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mOpenColorState));
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(getNormalColor(mOpenColorState));
        animation.setToStrokeColor(getNormalColor(mInstallColorState));

        animation.setListener(mInstallStateListener);

        animation.start();

    }

    private void morphUpdateToInstall() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mUpdateColorState));
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(getNormalColor(mUpdateColorState));
        animation.setToStrokeColor(getNormalColor(mInstallColorState));

        animation.setListener(mInstallStateListener);

        animation.start();

    }

    private void morphDownladToInstall() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mInstallColorState));
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(getNormalColor(mInstallColorState));
        animation.setToStrokeColor(getNormalColor(mInstallColorState));

        animation.setListener(mInstallStateListener);

        animation.start();
    }

    private OnAnimationEndListener mInstallStateListener = new OnAnimationEndListener() {

        @Override
        public void onAnimationEnd() {
            removeIcon();
            setText(mInstallText);
            mMorphingInProgress = false;
            mState = State.INSTALL;
        }
    };

    private void morphInstallToUpdate() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mInstallColorState));
        animation.setToColor(getNormalColor(mUpdateColorState));

        animation.setFromStrokeColor(getNormalColor(mInstallColorState));
        animation.setToStrokeColor(getNormalColor(mUpdateColorState));

        animation.setListener(mUpdateStateListener);

        animation.start();

    }

    private void morphProgressToUpdate() {

        MorphingAnimation animation = createProgressMorphing(getHeight(), mCornerRadius, getHeight(), getWidth());

        animation.setFromColor(mColorProgress);
        animation.setToColor(getNormalColor(mUpdateColorState));

        animation.setFromStrokeColor(mColorIndicator);
        animation.setToStrokeColor(getNormalColor(mUpdateColorState));
        animation.setListener(mUpdateStateListener);

        animation.start();
    }

    private void morphOpenToUpdate() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mOpenColorState));
        animation.setToColor(getNormalColor(mUpdateColorState));

        animation.setFromStrokeColor(getNormalColor(mOpenColorState));
        animation.setToStrokeColor(getNormalColor(mUpdateColorState));

        animation.setListener(mUpdateStateListener);

        animation.start();
    }

    private void morphDownloadToUpdate() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mInstallColorState));
        animation.setToColor(getNormalColor(mUpdateColorState));

        animation.setFromStrokeColor(getNormalColor(mInstallColorState));
        animation.setToStrokeColor(getNormalColor(mUpdateColorState));

        animation.setListener(mUpdateStateListener);

        animation.start();
    }

    private OnAnimationEndListener mUpdateStateListener = new OnAnimationEndListener() {

        @Override
        public void onAnimationEnd() {
            if (mIconError != 0) {
                setText(null);
                setIcon(mIconError);
            } else {
                setText(mUpdateText);
            }
            mMorphingInProgress = false;
            mState = State.UPDATE;
        }
    };

    private void morphProgressToInstall() {

        MorphingAnimation animation = createProgressMorphing(getHeight(), mCornerRadius, getHeight(), getWidth());
        animation.setFromColor(mColorProgress);
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(mColorIndicator);
        animation.setToStrokeColor(getNormalColor(mInstallColorState));
        animation.setListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                removeIcon();
                setText(mInstallText);
                mMorphingInProgress = false;
                mState = State.INSTALL;
            }
        });
        animation.start();
    }

    private void morphProgressToDownload() {

        MorphingAnimation animation = createProgressMorphing(getHeight(), mCornerRadius, getHeight(), getWidth());
        animation.setFromColor(mColorProgress);
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(mColorIndicator);
        animation.setToStrokeColor(getNormalColor(mInstallColorState));
        animation.setListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                removeIcon();
                setText(mDownloadText);
                mMorphingInProgress = false;
                mState = State.DOWNLOAD;
            }
        });
        animation.start();
    }

    private void morphOpenToDownload() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mOpenColorState));
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(getNormalColor(mOpenColorState));
        animation.setToStrokeColor(getNormalColor(mInstallColorState));

        animation.setListener(mDownloadStateListener);

        animation.start();
    }

    private void morphUpdateToDownload() {


        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mUpdateColorState));
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(getNormalColor(mUpdateColorState));
        animation.setToStrokeColor(getNormalColor(mInstallColorState));

        animation.setListener(mDownloadStateListener);

        animation.start();
    }

    private void morphInstallToDownload() {

        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getNormalColor(mInstallColorState));
        animation.setToColor(getNormalColor(mInstallColorState));

        animation.setFromStrokeColor(getNormalColor(mInstallColorState));
        animation.setToStrokeColor(getNormalColor(mInstallColorState));

        animation.setListener(mDownloadStateListener);

        animation.start();
    }

    private OnAnimationEndListener mDownloadStateListener = new OnAnimationEndListener() {

        @Override
        public void onAnimationEnd() {
            removeIcon();
            setText(mDownloadText);
            mMorphingInProgress = false;
            mState = State.DOWNLOAD;
        }
    };

    private void setIcon(int icon) {

        Drawable drawable = getResources().getDrawable(icon);
        if (drawable != null) {
            int padding = (getWidth() / 2) - (drawable.getIntrinsicWidth() / 2);
            setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
            setPadding(padding, 0, 0, 0);
        }
    }

    protected void removeIcon() {

        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        setPadding(0, 0, 0, 0);
    }

    /**
     * Set the View's background. Masks the API changes made in Jelly Bean.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    public State getState() {

        return this.mState;
    }

    public synchronized void setState(State newState) {

        this.mNewState = newState;
        removeIcon();
        if (mMorphingInProgress || getWidth() == 0) {
            return;
        }

        if(newState == State.INSTALL) {
            if(mState == State.INSTALL) {
                setText(mInstallText);
            }
            if(mState == State.PROGRESS) {
                morphProgressToInstall();
            } else if(mState == State.OPEN) {
                morphOpenToInstall();
            } else if(mState ==  State.DOWNLOAD) {
                morphDownladToInstall();
            } else if(mState == State.UPDATE) {
                morphUpdateToInstall();
            }
        } else if(newState == State.PROGRESS) {
            if(mState == State.PROGRESS) {
                invalidate();
            } else if(mState == State.INSTALL) {
                morphToProgress();
            } else if(mState == State.DOWNLOAD) {
                morphToProgress();
            } else if(mState == State.UPDATE) {
                morphToProgress();
            } else if(mState == State.OPEN) {
                morphToProgress();
            }
        } else if(newState == State.DOWNLOAD) {
            if(mState == State.DOWNLOAD) {
                setText(mDownloadText);
            } else if(mState == State.PROGRESS) {
                morphProgressToDownload();
            } else if(mState == State.OPEN) {
                morphOpenToDownload();
            } else if(mState == State.UPDATE) {
                morphUpdateToDownload();
            } else if(mState == State.INSTALL) {
                morphInstallToDownload();
            }
        } else if(newState == State.OPEN) {
            if(mState == State.OPEN) {
                setText(mOpenText);
            } else if(mState == State.INSTALL) {
                morphInstallToOpen();
            } else if(mState == State.UPDATE) {
                morphUpdateToOpen();
            } else if(mState == State.DOWNLOAD) {
                morphDownloadToOpen();
            } else if(mState == State.PROGRESS) {
                morphProgressToOpen();
            }

        } else if(newState == State.UPDATE) {
            if(mState == State.UPDATE) {
                setText(mUpdateText);
            } else if(mState == State.OPEN) {
                morphOpenToUpdate();
            } else if(mState == State.PROGRESS) {
                morphProgressToUpdate();
            } else if(mState == State.DOWNLOAD) {
                morphDownloadToUpdate();
            } else if(mState == State.INSTALL) {
                morphInstallToUpdate();
            }
        }
    }

    public void setBackgroundColor(int color) {

        background.getGradientDrawable().setColor(color);
    }

    public void setStrokeColor(int color) {
        background.setStrokeColor(color);
    }

    public String getIdleText() {
        return mInstallText;
    }

    public String getCompleteText() {
        return mOpenText;
    }

    public String getErrorText() {
        return mUpdateText;
    }

    public void setIdleText(String text) {
        mInstallText = text;
    }

    public void setCompleteText(String text) {
        mOpenText = text;
    }

    public void setErrorText(String text) {
        mUpdateText = text;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            setState(mNewState);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mIndeterminateProgressMode = mIndeterminateProgressMode;
        savedState.mConfigurationChanged = true;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mIndeterminateProgressMode = savedState.mIndeterminateProgressMode;
            mConfigurationChanged = savedState.mConfigurationChanged;
            super.onRestoreInstanceState(savedState.getSuperState());
            setState(mState);
        } else {
            super.onRestoreInstanceState(state);
        }
    }


     static class SavedState extends BaseSavedState {

        private boolean mIndeterminateProgressMode;
        private boolean mConfigurationChanged;
        private int mProgress;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mProgress = in.readInt();
            mIndeterminateProgressMode = in.readInt() == 1;
            mConfigurationChanged = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mProgress);
            out.writeInt(mIndeterminateProgressMode ? 1 : 0);
            out.writeInt(mConfigurationChanged ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
