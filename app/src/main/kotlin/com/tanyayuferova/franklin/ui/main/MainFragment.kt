package com.tanyayuferova.franklin.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.di.Injection
import com.tanyayuferova.franklin.domain.main.MainViewModel
import com.tanyayuferova.franklin.ui.common.itemdecoration.ItemDivider
import com.tanyayuferova.franklin.ui.main.MainFragmentDirections.Companion.actionMainToStatistics
import com.tanyayuferova.franklin.utils.getDrawableCompat
import com.tanyayuferova.franklin.utils.getFirstDayOfWeek
import com.tanyayuferova.franklin.utils.requireValue
import com.tanyayuferova.franklin.utils.observe
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        Injection.provideMainViewModelFactory(requireContext())
    }

    private lateinit var virtuesAdapter: VirtuesAdapter
    private lateinit var weeksSliderAdapter: WeeksSliderAdapter
    private val weeksCount = 60
    private val weeksPageListener = object  : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            viewModel.onWeekSliderPageChanged(position - weeksCount / 2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureToolbar()
        configureRecycler()
        configureWeeksSlider()
        configureData()
    }

    private fun configureToolbar() = with(toolbar) {
        inflateMenu(R.menu.main)
        setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    findNavController().navigate(R.id.action_main_to_settings)
                    true
                }
                R.id.statistics -> {
                    val date = getFirstDayOfWeek(viewModel.selectedDate.requireValue)
                    findNavController().navigate(
                        actionMainToStatistics(date)
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun configureRecycler() = with(recycler) {
        virtuesAdapter = VirtuesAdapter(object : BaseVirtueAdapterDelegate.Callback {
            override fun onIconClick(virtueId: Int) {
                viewModel.onVirtueSelected(virtueId)
            }
            override fun onClick(virtueId: Int) {
                viewModel.onAddPoint(virtueId)
            }
            override fun onLongClick(virtueId: Int) {
                viewModel.onRemovePoint(virtueId)
            }
        })
        layoutManager = LinearLayoutManager(requireContext())
        adapter = virtuesAdapter
        addItemDecoration(
            ItemDivider(
                context = context,
                divider = context.getDrawableCompat(R.drawable.divider_horizontal_spacing_h)!!
            )
        )
        setHasFixedSize(true)
    }

    private fun configureWeeksSlider() = with(weeksSlider) {
        addOnPageChangeListener(weeksPageListener)
        weeksSliderAdapter = WeeksSliderAdapter(weeksCount).apply {
            onDayClickListener = viewModel::onSelectedDateChanged
        }
        adapter = weeksSliderAdapter
        currentItem = weeksCount / 2
    }

    private fun configureData() = with(viewModel) {
        toolbarTitle.observe(viewLifecycleOwner, toolbar::setTitle)
        selectedDate.observe(viewLifecycleOwner) { date ->
            weeksSliderAdapter.selectedDate = date
        }
        virtues.observe(viewLifecycleOwner) { virtues ->
            virtuesAdapter.submitList(virtues)
        }
    }

    override fun onDestroyView() {
        weeksSlider.removeOnPageChangeListener(weeksPageListener)
        super.onDestroyView()
    }
}
