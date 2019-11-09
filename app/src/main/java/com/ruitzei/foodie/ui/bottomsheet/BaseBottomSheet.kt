package com.ruitzei.foodie.ui.bottomsheet

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ruitzei.foodie.R
import com.ruitzei.foodie.utils.loadImage
import kotlinx.android.synthetic.main.layout_base_bottom_sheet.view.*

abstract class BaseBottomSheet: BottomSheetDialogFragment() {
    private var avatar: SimpleDraweeView? = null
    private var title: TextView? = null
    private var titleBelowToolbar: TextView? = null

    val childContentView by lazy {
        LayoutInflater.from(context).inflate(getLayoutId(), null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.AtmBottomSheetDialog)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val contentView = View.inflate(context, R.layout.layout_base_bottom_sheet, null)
        dialog?.setContentView(contentView)

        val params = (contentView.parent as View).layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    onBottomSheetSlided(bottomSheet, slideOffset)
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })

            if (showsFullHeight()) {
                behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            }
        }

        if (shouldShowToolbar()) {
            contentView.toolbar.apply {
                visibility = View.VISIBLE
                setNavigationIcon(R.drawable.ic_close_black_24dp)
                setNavigationOnClickListener {
                    dismiss()
                }
            }
        } else {
            contentView.toolbar.visibility = View.GONE
            isCancelable = false
        }

        avatar = contentView.base_bottom_avatar
        getAvatarUrl()?.let {
            setImageAvatar(it)
        }


        titleBelowToolbar = contentView.toolbar_title_avatar
        title = contentView.toolbar_title
        getTitle()?.let {
            setToolbarTitle(it)
        }

        onChildContentViewCreated()
        contentView.linear_parent_layout.addView(childContentView)

        afterChildContentViewAdded()?.let {
            contentView.linear_parent_layout.addView(it)
        }
    }

    protected fun setImageAvatar(url: String) {
        avatar?.loadImage(url)
    }

    protected fun setToolbarTitle(titleString: String) {
        if (getAvatarUrl().isNullOrEmpty()) {
            title?.text = titleString
        } else {
            titleBelowToolbar?.text = titleString
        }
    }

    open fun shouldShowContentViewToolbar(): Boolean {
        return false
    }

    /*
        This is the layout resource that will be inflated and saved on `childContentView`.
        It will be inflated below the toolbar.
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /*
        You can call from the subclasses `childContentView.<view_id>.<view_method>`, just like in any
        regular fragment.
        Remember to set the listeners and stuff on this method.
     */
    abstract fun onChildContentViewCreated()

    /*
        This method will be called after the child content view gets added to the linearlayout.
        It's ideal to add views that don't need to be in the layout (keyboard on the bottom
        sheet for eg.)
     */
    open fun afterChildContentViewAdded(): View? { return null }

    /*
        Set the avatar URL of the image shown on the center top.
     */
    abstract fun getAvatarUrl(): String?

    /*
        Set the avatar URL of the image shown on the center top.
     */
    abstract fun getTitle(): String?

    /*
        Perform any action on the `onSlide` call of the bottomSheet.
        Default impl. is empty to make it optional to override.
     */
    open fun onBottomSheetSlided(bottomSheet: View, slideOffset: Float) { }

    protected open fun showsFullHeight(): Boolean = true

    protected open fun shouldShowToolbar(): Boolean = true
}
