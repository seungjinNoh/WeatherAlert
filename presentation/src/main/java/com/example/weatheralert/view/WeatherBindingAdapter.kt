package com.example.weatheralert.view

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.MidWeatherEntity
import com.example.domain.model.ShortWeatherEntity
import com.example.weatheralert.R
import com.example.weatheralert.base.UiState
import com.example.weatheralert.base.successOrNull

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
    @BindingAdapter("toast")
    fun View.bindToast(throwable: Throwable?) {
        throwable?.message?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    @BindingAdapter("shortWeatherItem")
    fun RecyclerView.bindShortWeatherItem(uiState: UiState<List<ShortWeatherEntity>>) {
        uiState.successOrNull()?.let { data ->
            val adapter = ShortWeatherAdapter()
            this.adapter = adapter
            val currentDay = data.first().date

            adapter.submitList(data.filter { it.date.toInt() < currentDay.toInt() + 3})
            if (this.itemDecorationCount == 0) this.addItemDecoration(ShortWeatherAdapter.ShortWeatherItemDecoration())
        }
    }

    @JvmStatic
    @BindingAdapter("midWeatherItem")
    fun RecyclerView.bindMidWeatherItem(uiState: UiState<List<MidWeatherEntity>>) {
        uiState.successOrNull()?.let {
            val adapter = MidWeatherAdapter()
            this.adapter = adapter

            adapter.submitList(it)
            if (this.itemDecorationCount == 0) this.addItemDecoration(MidWeatherAdapter.MidWeatherItemDecoration())
        }
    }

    @JvmStatic
    @BindingAdapter("time", "sky", "pty")
    fun ImageView.bindCurrentSky(time: String?, sky: String?, pty: String?) {
        if (time != null && sky != null && pty != null) {
            this.setImageResource(
                when (sky) {
                    "1" -> if ((time.toInt() / 100) in 7..18) R.drawable.sun else R.drawable.moon
                    "3" -> if ((time.toInt() / 100) in 7..18) R.drawable.sun_cloudy else R.drawable.moon_cloudy
                    else -> {
                        when (pty) {
                            "1", "4" -> R.drawable.rain
                            "2" -> R.drawable.snow
                            "3" -> R.drawable.rain_snow
                            else -> R.drawable.cloud
                        }
                    }
                }
            )
        }
    }



}