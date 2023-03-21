package com.example.currencyconverter.currency.history.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.currency.history.chart.databinding.CurrencyChartFragmentBinding
import com.example.currencyconverter.util.Util
import com.github.mikephil.charting.charts.LineChart
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
            setUpChartGestureListener(chart)

            val entries: MutableList<Entry> = ArrayList()

            for (date in rateByDateMap.keys) {
                val x = rateByDateMap[date]!![0]
                val y = rateByDateMap[date]!![1]
                entries.add(Entry(x,y))
            }

            val dataSet = LineDataSet(entries, "Rate")
            dataSet.setDrawCircles(false)
            dataSet.color = resources.getColor(com.example.currencyconverter.brandkit.R.color.currency_converter_light_900)
            dataSet.valueTextColor = Color.GRAY

            val lineData = LineData(dataSet)
            val markerView = CustomMarkerView(requireContext(), R.layout.marker_view)
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
            yLeftAxis.setDrawAxisLine(false)
            yLeftAxis.setDrawGridLines(false)
            yLeftAxis.setLabelCount(3, true)

            val yRightAxis = chart.axisRight
            yRightAxis.setDrawLabels(false)
            yRightAxis.setDrawAxisLine(false)
            yRightAxis.setDrawGridLines(false)
        } else {
            // TODO - Display error message
        }
    }

    private fun setUpChartGestureListener(chart: LineChart) {
        chart.onChartGestureListener = object: OnChartGestureListener {
            override fun onChartGestureStart(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {}

            override fun onChartGestureEnd(
                me: MotionEvent?,
                lastPerformedGesture: ChartTouchListener.ChartGesture?
            ) {}

            override fun onChartLongPressed(me: MotionEvent?) {}

            override fun onChartDoubleTapped(me: MotionEvent?) {
                chart.setScaleEnabled(false) // Prevent zoom-in on double tap
            }

            override fun onChartSingleTapped(me: MotionEvent?) {}

            override fun onChartFling(
                me1: MotionEvent?,
                me2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ) {}

            override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {}

            override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}

        }
    }

    /**
     * Parse the given historical rates JSON object and generates a SortedMap that has the date as
     * the key and the graph plots (x,y) as the value, where x is a floater index and y is the
     * rate for the respective date
     */
    private fun buildSortedHistoricalRatesDataSetMap(): SortedMap<String, List<Float>> {
        val ratesDataSet = HashMap<String, List<Float>>()
        var floaterIndex = 0f

        if (historicalRates.getBoolean("success")) {
            val dateToRateJsonObject = historicalRates.getJSONObject("rates")

            for (date in dateToRateJsonObject.keys()) {
                val rate = dateToRateJsonObject.getJSONObject(date).getDouble(toCurrencySymbol)
                ratesDataSet[date] = listOf(floaterIndex, rate.toFloat())
                floaterIndex++
            }
        }

        return ratesDataSet.toSortedMap(
            compareBy { Util.getCurrentDateFromString("yyyy-MM-dd", it) }
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