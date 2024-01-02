package technical.test.yprsty.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import technical.test.yprsty.data.ToDoRepository
import technical.test.yprsty.model.Activity
import technical.test.yprsty.model.GeoIp

class DetailViewModel(private val repository: ToDoRepository) : ViewModel() {

    private val _uiState = MutableLiveData<DetailUiState>(DetailUiState.Loading)
    val uiState: LiveData<DetailUiState> = _uiState

    private val _geoInfo = MutableLiveData<GeoIp>()
    val geoInfo: LiveData<GeoIp> = _geoInfo

    private val _errorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable> = _errorMessage

    init {
        loadActivities()
        fetchGeoInfo()
    }

    fun loadActivities() {
        viewModelScope.launch {
            repository.loadAllActivity()
                .catch { e ->
                    e.printStackTrace()
                    _uiState.postValue(DetailUiState.Error(e))
                }
                .collect {
                    _uiState.postValue(DetailUiState.Success(it))
                }
        }
    }

    fun searchActivityByTitle(title: String) {
        viewModelScope.launch {
            repository.searchActivityByTitle(title)
                .catch { e ->
                    e.printStackTrace()
                    _uiState.postValue(DetailUiState.Error(e))
                }
                .collect {
                    _uiState.postValue(DetailUiState.Success(it))
                }
        }
    }

    fun fetchGeoInfo() {
        viewModelScope.launch {
            repository.loadGeoApi()
                .catch { e ->
                    e.printStackTrace()
                    _errorMessage.postValue(e)
                }
                .collect {
                    _geoInfo.postValue(it)
                }
        }
    }
}

sealed class DetailUiState {
    data class Success(val activities: List<Activity>) : DetailUiState()
    data class Error(val exception: Throwable) : DetailUiState()
    data object Loading : DetailUiState()
}