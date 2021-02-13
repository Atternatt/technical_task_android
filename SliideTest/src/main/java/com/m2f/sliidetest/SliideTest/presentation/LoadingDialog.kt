package com.m2f.sliidetest.SliideTest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.m2f.sliidetest.SliideTest.databinding.DialogLoadingBinding

class LoadingDialog: DialogFragment() {

    companion object {
        const val TAG = "Loading"
    }

    private lateinit var binding: DialogLoadingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

}