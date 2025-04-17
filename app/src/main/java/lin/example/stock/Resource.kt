package lin.example.stock

/**
 * 統一封裝執行結果
 */
sealed class Resource<out T> {
    /** 載入中 */
    data object Loading : Resource<Nothing>()

    /** 成功，附帶資料 */
    data class Success<T>(val data: T) : Resource<T>()

    /** 失敗，附帶錯誤訊息與例外 */
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
}