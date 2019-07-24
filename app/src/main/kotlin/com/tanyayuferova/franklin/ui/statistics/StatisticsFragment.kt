package com.tanyayuferova.franklin.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.di.Injection
import com.tanyayuferova.franklin.domain.statistics.StatisticsViewModel
import com.tanyayuferova.franklin.utils.observe
import kotlinx.android.synthetic.main.fragment_statistics.*
import com.tanyayuferova.franklin.ui.common.itemdecoration.CustomPositionItemDivider
import com.tanyayuferova.franklin.utils.getDrawableCompat
import com.tanyayuferova.franklin.utils.now

/**
 * Author: Tanya Yuferova
 * Date: 7/8/19
 */
class StatisticsFragment : Fragment() {

    private val args: StatisticsFragmentArgs by navArgs()

    private val viewModel: StatisticsViewModel by viewModels {
        Injection.provideStatisticsViewModelFactory(requireContext(), args.date)
    }

    private val statisticsAdapter = StatisticsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureToolbar()
        configureRecycler()
        configureButtons()
        configureData()
    }

    private fun configureToolbar() = with(toolbar) {
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun configureRecycler() = with(recycler) {
        adapter = statisticsAdapter
        layoutManager = LinearLayoutManager(requireContext())
        setHasFixedSize(true)
        addItemDecoration(
            CustomPositionItemDivider(
                context = requireContext(),
                positions = intArrayOf(1),
                divider = context.getDrawableCompat(R.drawable.divider_horizontal_spacing_h)!!
            )
        )
    }

    private fun configureButtons() {
        nextWeekButton.setOnClickListener {
            viewModel.onNextWeekSelected()
        }
        previousWeekButton.setOnClickListener {
            viewModel.onPreviousWeekSelected()
        }
    }

    private fun configureData() = with(viewModel) {
        virtues.observe(viewLifecycleOwner, statisticsAdapter::submitList)
        toolbarTitle.observe(viewLifecycleOwner, weekTitle::setText)
        period.observe(viewLifecycleOwner) { (_, end) ->
            nextWeekButton.isEnabled = end.before(now)
        }
    }
}
