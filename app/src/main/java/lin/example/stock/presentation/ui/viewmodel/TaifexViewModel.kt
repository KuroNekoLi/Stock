package lin.example.stock.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lin.example.stock.Resource
import lin.example.stock.domain.model.StockUiModel
import lin.example.stock.domain.usecase.FetchAllStockInfoUseCase
import lin.example.stock.presentation.ui.model.StockListScreenState
import lin.example.stock.presentation.ui.model.StockSortOption

class TaifexViewModel(
    private val useCase: FetchAllStockInfoUseCase
) : ViewModel() {
    // Backing StateFlow，持有 ScreenState
    private val _state = MutableStateFlow(StockListScreenState())
    val state: StateFlow<StockListScreenState> = _state.asStateFlow()

    init {
        loadStocks()
    }

    /** 依照當前 sortOption 載入資料並更新 resource */
    /** 依照當前 sortOption 載入資料並更新 resource，並套用預設排序 */
    fun loadStocks() {
        viewModelScope.launch {
            // 先將 state 設為 Loading
            _state.update { it.copy(resource = Resource.Loading) }
            // 請求 UseCase
            when (val result = useCase()) {
                is Resource.Success -> {
                    // 取出資料並依照目前 sortOption 排序（預設 CODE_DESC）
                    val sorted = sortList(result.data, _state.value.sortOption)
                    _state.update { it.copy(resource = Resource.Success(sorted)) }
                }

                is Resource.Error -> {
                    // 失敗就如實更新
                    _state.update { it.copy(resource = result) }
                }

                else -> { /* 不處理 Loading */
                }
            }
        }
    }

    /**
     * 使用者選擇排序方式後：
     *  1. 更新 sortOption
     *  2. 重新排序目前的資料（可改為直接重新 fetch 或 local sort）
     */
    fun onSortOptionSelected(option: StockSortOption) {
        _state.update { it.copy(sortOption = option) }
        // 若要 local sort：
        val currentData = (_state.value.resource as? Resource.Success)?.data ?: return
        val sorted = sortList(currentData, option)
        _state.update { it.copy(resource = Resource.Success(sorted)) }
    }

    /** 根據 [StockSortOption] 排序清單 */
    private fun sortList(
        list: List<StockUiModel>,
        option: StockSortOption
    ): List<StockUiModel> = when (option) {
        StockSortOption.CODE_ASC -> list.sortedBy { it.code }
        StockSortOption.CODE_DESC -> list.sortedByDescending { it.code }
        StockSortOption.CLOSE_ASC -> list.sortedBy { it.closePrice }
        StockSortOption.CLOSE_DESC -> list.sortedByDescending { it.closePrice }
        StockSortOption.DIFF_ASC -> list.sortedBy { it.change ?: 0.0 }
        StockSortOption.DIFF_DESC -> list.sortedByDescending { it.change ?: 0.0 }
    }
}