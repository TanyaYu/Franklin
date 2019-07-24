package com.tanyayuferova.franklin.ui.aboutPlan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tanyayuferova.franklin.R
import kotlinx.android.synthetic.main.fragment_about_plan.*

/**
 * Author: Tanya Yuferova
 * Date: 7/24/19
 */
class AboutPlanFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}