package com.example.currencyconverter.navigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.navigationbar.databinding.NavigationBarFragmentBinding

class NavigationBarFragment : Fragment(R.layout.navigation_bar_fragment) {

    private lateinit var binding: NavigationBarFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NavigationBarFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start the app with the convert fragment selected
        binding.navigationBar.selectedItemId = R.id.navigation_bar_menu_convert

        binding.navigationBar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_bar_menu_convert -> {
                    // handle navigation to convert screen
                    true
                }
                R.id.navigation_bar_menu_chart -> {
                    // handle navigation to chart screen
                    true
                }
                R.id.navigation_bar_menu_favorites -> {
                    // handle navigation to favorites screen
                    true
                }
                else -> false
            }
        }

        binding.navigationBar.setOnItemReselectedListener { item ->
            when(item.itemId) {
                R.id.navigation_bar_menu_convert -> {
                    // handle navigation to convert screen
                }
                R.id.navigation_bar_menu_chart -> {
                    // handle navigation to chart screen
                }
                R.id.navigation_bar_menu_favorites -> {
                    // handle navigation to favorites screen
                }
            }
        }
    }
}