package com.practicum.playlistmaker.createPlaylist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createPlaylist.ui.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.createPlaylist.ui.view_state.CreatePlaylistViewState
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreateBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class CreatePlaylistFragment: Fragment(R.layout.fragment_playlist_create) {

    private var _binding: FragmentPlaylistCreateBinding? = null
    private val binding: FragmentPlaylistCreateBinding
        get() = _binding!!

    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.playlistCover.clipToOutline = true

        createPlaylistViewModel.createPlaylistLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.playlistNameField.addTextChangedListener {
            createPlaylistViewModel.onNameChanged(it.toString())
            binding.playlistNameField.isHovered = it.toString().isNotBlank()
        }

        binding.playlistDescriptionField.addTextChangedListener {
            createPlaylistViewModel.onDescriptionChanged(it.toString())
            binding.playlistDescriptionField.isHovered = it.toString().isNotBlank()
        }

        binding.buttonBack.setNavigationOnClickListener {

            if (!createPlaylistViewModel.isValueEmpty()) {
                confirmExit()
            }
            else {
                findNavController().popBackStack()
            }

        }

        val pickMedia =

            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

                createPlaylistViewModel.onPickImage(uri = uri)
            }

        binding.playlistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.playlistCreate.setOnClickListener {

            createPlaylistViewModel.createPlaylist()

            Toast.makeText(requireContext(), "Плейлист ${createPlaylistViewModel.createPlaylistLiveData.value!!.name} создан",
                Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()

        }

    }

    fun render (state: CreatePlaylistViewState) {

        binding.playlistCreate.isEnabled = state.ready

        if (state.uri != null) {
            binding.playlistCover.setImageURI(state.uri)
            binding.placeHolder.visibility = View.GONE

        } else {
            binding.playlistCover.setImageURI(state.uri)
            binding.placeHolder.visibility = View.VISIBLE
        }

    }

    fun confirmExit() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?") // Заголовок диалога
            .setMessage("Все несохраненные данные будут потеряны") // Описание диалога

            .setNegativeButton("Отмена") { dialog, which -> // Добавляет кнопку «Нет»
            }
            .setPositiveButton("Завершить") { dialog, which -> // Добавляет кнопку «Да»
                findNavController().popBackStack()
            }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}