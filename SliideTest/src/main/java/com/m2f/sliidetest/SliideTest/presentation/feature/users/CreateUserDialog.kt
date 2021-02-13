package com.m2f.sliidetest.SliideTest.presentation.feature.users

import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.m2f.sliidetest.SliideTest.databinding.DialogCreateUserBinding


class CreateUserDialog : DialogFragment() {

    companion object {
        const val TAG = "CreateUserDialog"
    }

    private lateinit var binding: DialogCreateUserBinding

    interface OnUserCreatedListener {
        fun onParametersSet(name: String, email: String, gender: String)
    }

    private var listener: OnUserCreatedListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateForm()

        binding.genderToggle.check(binding.male.id)

        binding.name.addTextChangedListener {
            validateForm()
        }

        binding.email.addTextChangedListener {
            validateForm()
        }

        binding.buttonCreateUser.setOnClickListener {
            val gender = when (binding.genderToggle.checkedButtonId) {
                binding.female.id -> "Female"
                else -> "Male"
            }
            listener?.onParametersSet(binding.name.text.toString(), binding.email.text.toString(), gender)
            dismiss()
        }
    }

    private fun validateForm() {
        val validName = binding.name.text?.isNotBlank() ?: false
        val isValidEmail = binding.email.text?.run {
            isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches().also {
                if (!it && isNotBlank()) {
                    binding.textFielEmail.error = "Invalid Email"
                } else {
                    binding.textFielEmail.error = null
                }

            }
        } ?: false

        binding.buttonCreateUser.isEnabled = validName && isValidEmail
    }

    fun show(fm: FragmentManager, tag: String, listener: OnUserCreatedListener? = null) {
        this.listener = listener
        super.show(fm, tag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener = null
    }
}