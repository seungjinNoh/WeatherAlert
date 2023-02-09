package com.example.domain.usecase

import com.example.domain.model.MidWeatherEntity
import com.example.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMidTmpUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend fun execute(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ) : Flow<List<MidWeatherEntity.MidTmpWeatherEntity>> = repository.getMidTmpWeather(numOfRows, pageNo, dataType, regId, tmFc)
}