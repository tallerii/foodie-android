package com.ruitzei.foodie.ui.modal

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.RatingModel
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.service.Api
import com.ruitzei.foodie.service.RequestCallbacks
import kotlinx.android.synthetic.main.comment_modal.*

open class RatingModal : DialogFragment() {
    private val TAG: String = RatingModal::class.java.simpleName

    protected var listener: ModalButtonListener? = null
    private var ratingModel: RatingModel? = null
    private var orderId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            ratingModel = it.getParcelable(RatingModel.TAG)
            orderId = it.getString("id")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        dialog.setContentView(R.layout.comment_modal)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.comment_modal, null)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratingModel?.let {
            hideStuff()

            modal_old_comment.text = "\"${it.notes}\""
            modal_rating.setIsIndicator(true)

            if (it.value >= 0) {
                modal_rating.rating = it.value.toFloat()
            } else {
                modal_rating.visibility = View.GONE
            }
        }

        modal_send.setOnClickListener {
            val ratingModel = RatingModel(notes = modal_text.text.toString(),
                order = orderId!!,
                user = UserData.user?.id!!,
                value = modal_rating.rating.toInt())

            listener?.onSendSelected(
                this,
                ratingModel
            )

            Api.addReviewToOrder(ratingModel, object: RequestCallbacks<RatingModel> {
                override fun onSuccess(response: RatingModel) {
                    dialog?.dismiss()
                    Toast.makeText(context, "Puntuación enviada", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(error: String?, code: Int, t: Throwable) {
                    Toast.makeText(context, "Hubo un error :(", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun hideStuff() {
        modal_send.visibility = View.GONE
        modal_text_input.visibility = View.GONE
        modal_subtitle.visibility = View.GONE
        modal_title.visibility = View.GONE

        modal_old_comment.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(
            ratingModel: RatingModel? = null,
            orderId: String? = null
        ): RatingModal {
            return RatingModal().apply {
                arguments = Bundle().apply {
                    ratingModel?.let {
                        putParcelable(RatingModel.TAG, ratingModel)
                    }
                    orderId?.let {
                        putString("id", it)
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = context as ModalButtonListener
        } catch (e: ClassCastException) {

        }
    }
}

interface ModalButtonListener {
    fun onSendSelected(modal: DialogFragment, ratingModel: RatingModel)
}