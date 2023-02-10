package com.example.weatheralert.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.domain.model.MidWeatherEntity
import com.example.domain.model.WeatherEntity
import com.example.domain.usecase.GetMidSkyUseCase
import com.example.domain.usecase.GetMidTmpUseCase
import com.example.domain.usecase.GetWeatherUseCase
import com.example.weatheralert.base.BaseViewModel
import com.example.weatheralert.base.UiState
import com.example.weatheralert.util.WeatherUtil
import com.example.weatheralert.view.WeatherActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.min


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getMidTmpWeatherUseCase: GetMidTmpUseCase,
    private val getMidSkyUseCase: GetMidSkyUseCase
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<WeatherEntity>>> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _addressState: MutableStateFlow<String> = MutableStateFlow("")
    val addressState = _addressState.asStateFlow()

    private val _currentWeather: MutableStateFlow<WeatherEntity?> = MutableStateFlow(null)
    val currentWeather = _currentWeather.asStateFlow()

    private val _midWeatherUiState: MutableStateFlow<UiState<List<MidWeatherEntity>>> = MutableStateFlow(UiState.Loading)
    val midWeatherUiState = _midWeatherUiState.asStateFlow()

    //단기예보 조회
    fun getWeather(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        base_date: Int,
        base_time: String,
        nx: String,
        ny: String
    ) {
        viewModelScope.launch {
            getWeatherUseCase.execute(numOfRows, pageNo, dataType, base_date, base_time, nx, ny).onStart {
                Timber.d("단기예보 flow start")
                _uiState.value = UiState.Loading
            }.catch {
                Timber.d("단기예보 flow catch")
                _uiState.value = UiState.Error(null)
            }.collect {
                Timber.d("단기예보 collect it: $it")
                _uiState.value = UiState.Success(it)
                _currentWeather.value = it.first()
            }
        }
    }

    //중기예보 조회
    fun getMidWeather(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ) {
        viewModelScope.launch {
            val midUiStateFlow = getMidTmpWeatherUseCase.execute(numOfRows, pageNo, dataType, regId, tmFc)
                .zip(getMidSkyUseCase.execute(numOfRows, pageNo, dataType, regId, tmFc)) { tmpFlow, skyFlow ->
                    val resultList = mutableListOf<MidWeatherEntity>()
                    for (i in 0 until min(tmpFlow.size, skyFlow.size)) {
                        resultList.add(MidWeatherEntity(tmpFlow[i], skyFlow[i]))
                    }
                    resultList
                }

            midUiStateFlow.onStart {
                Timber.d("중기예보 flow start")
                _midWeatherUiState.value = UiState.Loading
            }.catch {
                Timber.d("중기예보 flow catch")
                _midWeatherUiState.value = UiState.Error(null)
            }.collect {
                Timber.d("중기에보 데이터 collect: $it")
                _midWeatherUiState.value = UiState.Success(it)
            }
        }
    }

    fun getAddress(context: Context) {
        viewModelScope.launch {
            WeatherUtil.susGetAddress(context as WeatherActivity).collect {
                Timber.d("WeatherViewModel getAddress() collect 안 it: $it")
                _addressState.value = it
            }
        }
    }

//    sealed class UiState<out T> {
//        object Loading: UiState<Nothing>()
//        data class Success<T>(val data: T): UiState<T>()
//        data class Error(val errorCode: String = ""): UiState<Nothing>()
//    }
}