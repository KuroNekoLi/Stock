package lin.example.stock.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lin.example.stock.Resource
import lin.example.stock.domain.model.StockUiModel
import lin.example.stock.domain.usecase.FetchAllStockInfoUseCase

class TaifexViewModel(
    private val useCase: FetchAllStockInfoUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<Resource<List<StockUiModel>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<StockUiModel>>> = _uiState.asStateFlow()

    init {
        loadStocks()
    }

    fun loadStocks() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            _uiState.value = useCase()
        }
    }
}