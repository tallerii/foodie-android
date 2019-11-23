package com.ruitzei.foodie.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import com.facebook.login.LoginManager
import com.ruitzei.foodie.model.User
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.ui.login.LoginActivity
import com.ruitzei.foodie.utils.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {
    private var viewModel: ProfileViewModel? = null
    private var saveMenuItem: MenuItem? = null
    private var editMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(com.ruitzei.foodie.R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = UserData.user
        updateUserData(user)

        logout_btn.setOnClickListener {
            LoginManager.getInstance().logOut()
            PreferenceManager.removeToken()

            val intent = Intent(context!!, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun updateUserData(user: User?) {
        profile_name.setText(user?.fullName)
        profile_phone.setText(user?.phoneNumber)
        profile_email.setText(user?.mail)
        profile_rol.setText(if (user?.isDelivery == true) "Delivery" else "Usuario")
//        profile_subscription.setText(user?.isDelivery.toString())
        profile_balance.setText(user?.balance.toString())

        profile_avatar.loadImage(user?.avatar?: "")

        profile_change_avatar_btn.setOnClickListener {
            viewModel?.changeAvatar("")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            com.ruitzei.foodie.R.id.menu_edit -> {
                activity?.runOnUiThread {
                    saveMenuItem?.isVisible = true
                    editMenuItem?.isVisible = false

                    setEnabled()
                }
            }
            com.ruitzei.foodie.R.id.menu_save -> {
                saveMenuItem?.isVisible = false
                editMenuItem?.isVisible = true
                activity?.runOnUiThread {
                    setDisabled()
                }

                val name = profile_name
                val phone = profile_phone
                val email= profile_email
                viewModel?.updateFields(name.text.toString(), phone.text.toString(), email.text.toString())
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()

        inflater?.inflate(com.ruitzei.foodie.R.menu.profile_menu, menu)

        saveMenuItem = menu?.findItem(com.ruitzei.foodie.R.id.menu_save)
        editMenuItem = menu?.findItem(com.ruitzei.foodie.R.id.menu_edit)
    }

    private fun setEnabled() {
        profile_name.isEnabled = true
        profile_phone.isEnabled = true
        profile_email.isEnabled = true
    }

    private fun setDisabled() {
        activity?.closeKeyboard()
        profile_name.isEnabled = false
        profile_phone.isEnabled = false
        profile_email.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider()

        viewModel?.userAvatar?.observe(this, Observer { data ->
            data?.let {
                when (data.status) {
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.ERROR -> {

                    }
                    Resource.Status.SUCCESS -> {
                        profile_avatar.loadImage(it.data!!)
                    }
                }
            }
        })

        viewModel?.userUpdateResponse?.observe(this, Observer { data ->
            data?.let {
                when (data.status) {
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.ERROR -> {

                    }
                    Resource.Status.SUCCESS -> {
                        updateUserData(it.data)
                    }
                }
            }
        })
    }

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName

        fun newInstance(): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}