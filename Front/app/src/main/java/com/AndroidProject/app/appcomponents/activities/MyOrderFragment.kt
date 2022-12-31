package com.androidproject.app.appcomponents.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.Order

class MyOrderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val order: Order = requireArguments().getSerializable("EXTRA_ORDER") as Order

    }
}