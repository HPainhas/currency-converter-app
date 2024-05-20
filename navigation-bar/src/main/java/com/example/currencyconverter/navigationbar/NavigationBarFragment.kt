package com.example.currencyconverter.navigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.currencyconverter.api.ApiResponseCallback
import com.example.currencyconverter.currency.conversion.CurrencyConversionFragment
import com.example.currencyconverter.currency.favorite.CurrencyFavoriteFragment
import com.example.currencyconverter.currency.history.chart.CurrencyHistoryChartFragment
import com.example.currencyconverter.navigationbar.databinding.NavigationBarFragmentBinding
import com.example.currencyconverter.util.ProgressBarViewModel

class NavigationBarFragment : Fragment(R.layout.navigation_bar_fragment), ApiResponseCallback {

    private lateinit var binding: NavigationBarFragmentBinding

    private val progressBarViewModel: ProgressBarViewModel by activityViewModels()

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

        if (savedInstanceState == null) {
            setUpProgressBarObserver()
            setUpNavigationBarMenuItemListeners()
            replaceFragment(CurrencyConversionFragment())
            updateActionBar(CurrencyConversionFragment().javaClass.simpleName)

            // Start the app with the convert fragment menu item selected
            binding.navigationBar.selectedItemId = R.id.navigation_bar_menu_convert
        }
    }

    private fun setUpProgressBarObserver() {
        progressBarViewModel.showProgressBar.observe(viewLifecycleOwner) { show ->
            if (show) {
                binding.navigationBarDimmingOverlay.dimmingOverlay
                    .animate().alpha(0.5f).setDuration(200).start()
            }

            binding.navigationBarDimmingOverlay.dimmingOverlay.visibility =
                if (show) View.VISIBLE else View.GONE
            binding.navigationBarProgressBar.progressBar.visibility =
                if (show) View.VISIBLE else View.GONE
        }
    }

    private fun setUpNavigationBarMenuItemListeners() {
        binding.navigationBar.setOnItemSelectedListener OnItemSelectedListener@ { item ->
            val fragment = when(item.itemId) {
                R.id.navigation_bar_menu_convert -> CurrencyConversionFragment()
                R.id.navigation_bar_menu_chart -> CurrencyHistoryChartFragment()
                R.id.navigation_bar_menu_favorites -> CurrencyFavoriteFragment()
                else -> return@OnItemSelectedListener false
            }

            replaceFragment(fragment)
            updateActionBar(fragment.javaClass.simpleName)
            return@OnItemSelectedListener true
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

    private fun updateActionBar(fragmentName: String) {
        val title = when(fragmentName) {
            "CurrencyConversionFragment" -> context?.getString(R.string.navigation_bar_toolbar_title_convert)
            "CurrencyHistoryChartFragment" -> context?.getString(R.string.navigation_bar_toolbar_title_chart)
            "CurrencyFavoriteFragment" ->context?.getString(R.string.navigation_bar_toolbar_title_favorites)
            else -> ""
        }

        binding.navigationBarToolbarTitle.text = title
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.navigation_bar_fragment_container_view, fragment)
        }
    }

    override fun onSuccessApiResponse(responseBody: String, identifier: String) { /* no-ops */ }

    override fun onFailureApiResponse(errorMessage: String) { /* no-ops */ }
}