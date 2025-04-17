package lin.example.stock

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import lin.example.stock.domain.repository.TaifexRepository
import lin.example.stock.presentation.ui.theme.StockTheme
import lin.example.stock.presentation.ui.viewmodel.TaifexViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: TaifexViewModel by inject()
    private val repository: TaifexRepository by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            repository.fetchDailyClosingAndMonthlyAverage().also { Log.i("LinLi", "fetchDailyClosingAndMonthlyAverage = $it") }
            repository.fetchDailyStockTradingInfo().also { Log.i("LinLi", "fetchDailyStockTradingInfo = $it") }
        }
        viewModel.loadTaifexData()
        setContent {
            val uiState = viewModel.uiState.collectAsState()
            Log.i("LinLi", "uiState = ${uiState.value}")

            StockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StockTheme {
        Greeting("Android")
    }
}