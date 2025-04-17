package lin.example.stock.presentation.ui.model

import lin.example.stock.domain.model.TaifexData

/**
 * UI 狀態封裝：載入中、成功或錯誤。
 */
sealed class TaifexUiState {
    /** 資料載入中狀態 */
    object Loading : TaifexUiState()

    /** 資料取得成功，含資料清單 */
    data class Success(val data: List<TaifexData>) : TaifexUiState()

    /** 資料取得失敗，含錯誤訊息 */
    data class Error(val message: String) : TaifexUiState()
}