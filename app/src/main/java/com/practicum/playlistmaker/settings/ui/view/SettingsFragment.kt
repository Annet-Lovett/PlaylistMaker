package com.practicum.playlistmaker.settings.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.settings.ui.view_states.SettingsScreenState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->

            settingsViewModel.changeTheme(checked)
        }


        binding.btnShareSettings.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        binding.btnWriteToSupportSettings.setOnClickListener {
            val message = getString(R.string.message_to_support)
            val subject = getString(R.string.subject_support)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("annamarkova143@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        binding.btnTermsOfUseSettings.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.link_to_offer))
            }

            startActivity(intent)
        }

        settingsViewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render (state: SettingsScreenState) {
        binding.themeSwitcher.isChecked = state.isDarkTheme
    }


}