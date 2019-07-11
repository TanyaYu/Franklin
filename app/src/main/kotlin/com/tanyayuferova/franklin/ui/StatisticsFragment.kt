package com.tanyayuferova.franklin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.di.Injection
import com.tanyayuferova.franklin.domain.statistics.StatisticsViewModel
import com.tanyayuferova.franklin.utils.observe
import kotlinx.android.synthetic.main.fragment_statistics.*
import timber.log.Timber

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class StatisticsFragment : Fragment() {

    private val args: StatisticsFragmentArgs by navArgs()

    private val viewModel: StatisticsViewModel by viewModels {
        Injection.provideStatisticsViewModelFactory(requireContext(), args.date)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.virtues.observe(viewLifecycleOwner) {
            Timber.d(it.toString())
        }
        viewModel.selectedDate.observe(viewLifecycleOwner) {
            Timber.d(it.toString())
        }
    }

}
