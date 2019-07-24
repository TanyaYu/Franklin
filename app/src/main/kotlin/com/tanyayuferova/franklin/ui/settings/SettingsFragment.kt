package com.tanyayuferova.franklin.ui.settings

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tanyayuferova.franklin.R
import com.tanyayuferova.franklin.di.Injection
import com.tanyayuferova.franklin.domain.settings.SettingsViewModel
import com.tanyayuferova.franklin.utils.observe
import kotlinx.android.synthetic.main.fragment_settings.*

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
        configureToolBar()
        configureNotificationsSwitcher()
        configureNotificationsTime()
        configureAboutButtons()
        configureData()
    }

    private fun configureToolBar() = with(toolbar) {
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun configureNotificationsSwitcher() = with(notificationsSwitch) {
        setOnCheckedChangeListener { _, isChecked ->
            viewModel.ontNotificationFlagChanged(isChecked)
        }
    }

    private fun configureNotificationsTime() {
        viewModel.notificationsTime.observe(viewLifecycleOwner) { (hours, minutes) ->
            notificationTimeBlock.setOnClickListener {
                TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        viewModel.onNotificationTimeChanged(hour, minute)
                    },
                    hours,
                    minutes,
                    false
                ).show()
            }
        }
    }

    private fun configureAboutButtons() {
        aboutApp.setOnClickListener {

        }
        aboutPlan.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_aboutPlan)
        }
    }

    private fun configureData() = with(viewModel) {
        notificationsTimeString.observe(viewLifecycleOwner, notificationTime::setText)
        notificationsFlag.observe(viewLifecycleOwner) { isChecked ->
            notificationsSwitch.isChecked = isChecked
            notificationTimeBlock.isVisible = isChecked
            dividerTime.isVisible = isChecked
        }
    }

}
