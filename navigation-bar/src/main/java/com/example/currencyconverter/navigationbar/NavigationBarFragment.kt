package com.example.currencyconverter.navigationbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.currencyconverter.currency.conversion.CurrencyConversionFragment
import com.example.currencyconverter.currency.favorite.CurrencyFavoriteFragment
import com.example.currencyconverter.currency.history.chart.CurrencyHistoryChartFragment
import com.example.currencyconverter.currency.selection.CurrencyItemViewModels
import com.example.currencyconverter.navigationbar.databinding.NavigationBarFragmentBinding


class NavigationBarFragment : Fragment(R.layout.navigation_bar_fragment) {

    private lateinit var binding: NavigationBarFragmentBinding
    private lateinit var currencyItemViewModels: CurrencyItemViewModels

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

        arguments?.getSerializable("currencyItemViewModels")?.let {
            currencyItemViewModels = it as CurrencyItemViewModels
        }

        if (savedInstanceState == null) {
            setUpNavigationBarMenuItemListeners()
            replaceFragment(CurrencyConversionFragment.newInstance(currencyItemViewModels))
            updateActionBar(CurrencyConversionFragment().javaClass.simpleName)

            // Start the app with the convert fragment selected
            binding.navigationBar.selectedItemId = R.id.navigation_bar_menu_convert
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

    companion object {
        fun newInstance(currencyItemViewModels: CurrencyItemViewModels) =
            NavigationBarFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("currencyItemViewModels", currencyItemViewModels)
                }
            }
    }
}