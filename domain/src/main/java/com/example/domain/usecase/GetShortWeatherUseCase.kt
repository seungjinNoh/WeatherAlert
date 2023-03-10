package com.example.domain.usecase

import com.example.domain.model.ShortWeatherEntity
import com.example.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShortWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend fun execute(
        base_date: Int,
        base_time: String,
        nx: String,
        ny: String
    ): Flow<List<ShortWeatherEntity>> = repository.getShortWeather(base_date, base_time, nx, ny)

}