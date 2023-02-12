package com.example.weatheralert.view

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.MidWeatherEntity
import com.example.domain.model.ShortWeatherEntity
import com.example.weatheralert.R
import com.example.weatheralert.base.UiState

object WeatherBindingAdapter {

    @JvmStatic
    @BindingAdapter("isLoading")
    fun ProgressBar.isLoading(uiState: UiState<*>) {
        this.visibility = if (uiState is UiState.Loading) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("isShow")
    fun View.isShow(uiState: UiState<List<ShortWeatherEntity>>) {
        this.visibility = if (uiState is UiState.Loading) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("shortWeatherItem")
    fun RecyclerView.bindShortWeatherItem(uiState: UiState<List<ShortWeatherEntity>>) {
        if (uiState is UiState.Success) {
            val adapter = ShortWeatherAdapter()
            this.adapter = adapter
            val currentDay = uiState.data.first().date
            adapter.submitList(uiState.data.filter { it.date.toInt() < currentDay.toInt() + 3 })
            this.addItemDecoration(ShortWeatherAdapter.ShortWeatherItemDecoration())
        }
    }

    @JvmStatic
    @BindingAdapter("midWeatherItem")
    fun RecyclerView.bindMidWeatherItem(uiState: UiState<List<MidWeatherEntity>>) {
//        if (uiState is UiState.Success) {
//            val adapter = WeeklyWeatherAdapter()
//            this.adapter = adapter
//            var lastDay = ""
//            val weeklyList = mutableListOf<ShortWeatherEntity>()
//            uiState.data.forEach {
//                if (lastDay != it.date) {
//                    lastDay = it.date
//                    weeklyList.add(it)
//                }
//            }
//            adapter.submitList(weeklyList)
//        }
    }

    @JvmStatic
    @BindingAdapter("sky", "pty")
    fun ImageView.bindCurrentSky(sky: String?, pty: String?) {
        if (sky != null && pty != null) {
            this.setImageResource(when(sky) {
                "1" -> R.drawable.sun
                "3" -> R.drawable.sun_cloudy
                else -> {
                    when(pty) {
                        "1", "4" -> R.drawable.rain
                        "2" -> R.drawable.snow
                        "3" -> R.drawable.rain_snow
                        else -> R.drawable.cloud
                    }
                }
            })
        }
    }



}