package com.ndipatri.iot.zeroto98

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ndipatri.iot.zeroto98.api.ParticleAPI
import com.ndipatri.iot.zeroto98.databinding.FragmentFirstBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var particleAPI: ParticleAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ApplicationComponent.createIfNecessary().inject(this)

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateViewsWithButtonState()

        binding.buttonFirst.setOnClickListener { button ->
            lifecycleScope.launch {
                binding.textviewFirst.text = "Standby ..."
                if ((button as Button).text == "Turn Siren On") {
                    particleAPI.particleInterface.turnOnRedSiren()
                } else {
                    particleAPI.particleInterface.turnOffRedSiren()
                }
                updateViewsWithButtonState()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateViewsWithButtonState() {
        lifecycleScope.launch {
            val sirenState = particleAPI.particleInterface.getSirenState()
            binding.textviewFirst.text = "RedSiren is ${sirenState.result}!"
            binding.buttonFirst.text = "Turn Siren ${if (sirenState.result == "off") "On" else "Off"}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}