package com.example.currencyconverter.currency.history.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.currency.history.chart.databinding.CurrencyChartFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import org.json.JSONObject
import java.util.SortedMap

class CurrencyChartFragment : Fragment(R.layout.currency_chart_fragment) {

    private lateinit var binding: CurrencyChartFragmentBinding

    private val historicalRates by lazy {
        JSONObject(requireArguments().getString(ARG_HISTORICAL_RATES)!!)
    }

    private val toCurrencySymbol by lazy {
        requireArguments().getString(ARG_TO_CURRENCY_SYMBOL)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencyChartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setUpCurrencyChart()
        }
    }

    private fun setUpCurrencyChart() {
        val rateByDateMap = buildSortedHistoricalRatesDataSetMap()

        val chart = binding.currencyChart

        if (rateByDateMap.isNotEmpty()) {
            val entries: MutableList<Entry> = ArrayList()

            for (index in rateByDateMap.keys) {
                val x = index.toFloat()
                val y = rateByDateMap.values.elementAt(index).second
                entries.add(Entry(x,y))
            }

            val dataSet = LineDataSet(entries, "")
            dataSet.setDrawCircles(false)
            dataSet.form = Legend.LegendForm.NONE
            dataSet.color = resources.getColor(com.example.currencyconverter.brandkit.R.color.currency_converter_light_900)
            dataSet.valueTextColor = Color.GRAY

            val lineData = LineData(dataSet)
            val markerView = CustomMarkerView(
                requireContext(),
                R.layout.custom_marker_view,
                rateByDateMap
            )

            chart.data = lineData
            chart.marker = markerView
            chart.description = null
            chart.extraBottomOffset = 10f
            chart.isHighlightPerTapEnabled = true
            chart.invalidate() // refresh chart

            val xAxis = chart.xAxis
            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)

            val yLeftAxis = chart.axisLeft
            yLeftAxis.setDrawLabels(false)
            yLeftAxis.setDrawAxisLine(false)
            yLeftAxis.setDrawGridLines(false)

            val yRightAxis = chart.axisRight
            yRightAxis.setDrawLabels(false)
            yRightAxis.setDrawAxisLine(false)
            yRightAxis.setDrawGridLines(false)

            setUpChartGestureListener(chart)
        } else {
            // TODO - Display error message
        }
    }

    private fun setUpChartGestureListener(chart: LineChart) {
        chart.onChartGestureListener = object: OnChartGestureListener {
            override fun onChartGestureStart(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {
                // do nothing
            }

            override fun onChartGestureEnd(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {
                // do nothing
            }

            override fun onChartLongPressed(me: MotionEvent?) {
                // do nothing
            }

            override fun onChartDoubleTapped(me: MotionEvent?) {
                chart.setScaleEnabled(false) // Prevent zoom-in on double tap
            }

            override fun onChartSingleTapped(me: MotionEvent?) {
                // do nothing
            }

            override fun onChartFling(
                me1: MotionEvent?,
                me2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ) {}

            override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
                // do nothing
            }

            override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                // do nothing
            }

        }
    }

    /**
     * Parse the given historical rates JSON object and generates a SortedMap that has a Integer
     * index as the key and a Pair<String, Float> as the value, where String is the date and
     * Float is the rate for the respective date
     */
    private fun buildSortedHistoricalRatesDataSetMap(): SortedMap<Int, Pair<String,Float>> {
        val sortedMap = HashMap<Int, Pair<String,Float>>()
        var index = 0

        if (historicalRates.getBoolean("success")) {
            val dateToRateJsonObject = historicalRates.getJSONObject("rates")

            for (date in dateToRateJsonObject.keys()) {
                val rate = dateToRateJsonObject.getJSONObject(date).getDouble(toCurrencySymbol)
                sortedMap[index] = Pair(date, rate.toFloat())
                index++
            }
        }

        return sortedMap.toSortedMap(
            compareBy { it }
        )
    }

    companion object {

        private const val ARG_HISTORICAL_RATES = "CurrencyChartFragment.ARG_HISTORICAL_RATES"
        private const val ARG_TO_CURRENCY_SYMBOL = "CurrencyChartFragment.ARG_TO_CURRENCY_SYMBOL"

        fun newInstance(
            historicalRates: String,
            toCurrencySymbol: String
        ) : CurrencyChartFragment =
            CurrencyChartFragment().apply {
                this.arguments = Bundle().apply {
                    putString(ARG_HISTORICAL_RATES, historicalRates)
                    putString(ARG_TO_CURRENCY_SYMBOL, toCurrencySymbol)
                }
            }
    }
}