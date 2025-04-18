package lin.example.stock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import lin.example.stock.domain.repository.TaifexRepository
import lin.example.stock.presentation.ui.screen.StockListScreen
import lin.example.stock.presentation.ui.theme.StockTheme
import lin.example.stock.presentation.ui.viewmodel.TaifexViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: TaifexViewModel by inject()
    private val repository: TaifexRepository by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.loadStocks()
        setContent {
            val screenState by viewModel.state.collectAsState()

            StockTheme {
                StockListScreen(
                    screenState = screenState,
                    onSortSelect = { viewModel.onSortOptionSelected(it) }
                )
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