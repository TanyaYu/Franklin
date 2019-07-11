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
import com.tanyayuferova.franklin.domain.settings.SettingsViewModel
import com.tanyayuferova.franklin.utils.observe
import kotlinx.android.synthetic.main.fragment_settings.*
import timber.log.Timber

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels {
        Injection.provideSettingsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.notificationsFlag.observe(viewLifecycleOwner) { flag ->
            Timber.d(flag.toString())
        }
        viewModel.notificationsTime.observe(viewLifecycleOwner) { time ->
            Timber.d(time.toString())
        }
    }
}
