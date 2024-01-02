package technical.test.yprsty.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import technical.test.yprsty.data.ToDoRepository
import technical.test.yprsty.model.Activity

class MainViewModel(private val repository: ToDoRepository) : ViewModel() {

    private val _isAdded = MutableLiveData(false)
    val isAdded: LiveData<Boolean> = _isAdded

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        error.printStackTrace()
        _error.postValue(error.message)
        _isAdded.postValue(false)
    }

    fun addActivity(activity: Activity) {
        viewModelScope.launch(coroutineExceptionHandler) {
            repository.insertActivity(activity)
            _isAdded.postValue(true)
        }
    }
}