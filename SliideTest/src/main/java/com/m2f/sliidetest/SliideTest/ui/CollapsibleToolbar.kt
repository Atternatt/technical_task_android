package com.m2f.sliidetest.SliideTest.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.vectordrawable.graphics.drawable.SeekableAnimatedVectorDrawable
import com.m2f.sliidetest.SliideTest.R
import com.m2f.sliidetest.SliideTest.databinding.ViewCollapsibleToolbarBinding

/**
 * @author Marc Moreno
 *
 * @since 7/12/2020
 *
 */
class CollapsibleToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        val DEFAULT_ANIMATION_DRAWABLE = R.drawable.header_anim
        const val MIN_PROGRESS_RANGE = 0.0
        const val MAX_PROGRESS_RANGE = 1.0
    }

    private val binding: ViewCollapsibleToolbarBinding = ViewCollapsibleToolbarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var animationDrawable: SeekableAnimatedVectorDrawable? = null
        set(value) {
            field = value
            headerAnimationDuration = value?.totalDuration ?: 0
        }

    private var headerAnimationDuration: Long = 0

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CollapsibleToolbar, 0, 0
        )

        try {
            a.getString(R.styleable.CollapsibleToolbar_title)?.also(::setTitle)
            a.getFloat(R.styleable.CollapsibleToolbar_animationProgress, 0f)
                .also(::setAnimationProgress)
            val drawable = a.getResourceId(
                R.styleable.CollapsibleToolbar_header,
                R.drawable.header_anim
            )
            setAnimationDrawable(drawable)
        } finally {
            a.recycle()
        }

    }

    private fun Float.frame(): Long {
        val frame = (this * headerAnimationDuration).toLong()
        return frame
    }

    //region public API

    /** Sets the animation drawable for this component.
     * @param res the resource of the animation.
     * */
    fun setAnimationDrawable(@DrawableRes res: Int) {
        animationDrawable = SeekableAnimatedVectorDrawable.create(context, res)
        if (animationDrawable == null) {
            animationDrawable =
                SeekableAnimatedVectorDrawable.create(context, DEFAULT_ANIMATION_DRAWABLE)
        }
        binding.headerImage.setImageDrawable(animationDrawable)
    }

    /**
     * Sets the title for this component.
     * @param stringRes the string resource for the text.
     * */
    fun setTitle(@StringRes stringRes: Int) {
        binding.headerTitle.setText(stringRes)
    }

    /**
     * Sets the title for this component.
     * @param string the string for the text.
     * */
    fun setTitle(string: String) {
        binding.headerTitle.text = string
    }

    /**
     * Sets the progress of the animation.
     * @param progress current progress of the animation, values from 0 to 1.
     */
    fun setAnimationProgress(@FloatRange(from = MIN_PROGRESS_RANGE, to = MAX_PROGRESS_RANGE) progress: Float) {
        animationDrawable?.currentPlayTime = progress.frame()
    }

    /**
     * Gets the current animation progress.
     * @return the progress of the animation.
     */
    fun getAnimationProgress(): Float {
        return animationDrawable?.currentPlayTime?.toFloat() ?: 0f
    }

    /**
     * Sets the progress of the whole MotionLayout.
     * @param progress current progress of the motion, values from 0 to 1.
     */
    fun setProgress(@FloatRange(from = MIN_PROGRESS_RANGE, to = MAX_PROGRESS_RANGE) progress: Float) {
        binding.root.progress = progress
    }

    /**
     * Gets the current motion progress of teh component.
     * @return the progress of the motion.
     */
    fun getProgress(): Float = binding.root.progress
    //endregion
}