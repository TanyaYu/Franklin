package com.tanyayuferova.franklin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.di.Injection
import com.tanyayuferova.franklin.domain.main.MainViewModel
import com.tanyayuferova.franklin.ui.MainFragmentDirections.Companion.actionMainFragmentToStatisticsFragment
import com.tanyayuferova.franklin.utils.requireValue
import com.tanyayuferova.franklin.utils.observe
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        Injection.provideMainViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.inflateMenu(R.menu.main)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                    true
                }
                R.id.statistics -> {
                    val date = viewModel.selectedDate.requireValue
                    findNavController().navigate(
                        actionMainFragmentToStatisticsFragment(date)
                    )
                    true
                }
                else -> false
            }
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            Timber.d("current Date $date")
        }
        viewModel.virtues.observe(viewLifecycleOwner) { virtues ->
            Timber.d(virtues.toString())
        }
    }
}
