package lin.example.stock

import android.app.Application
import lin.example.stock.data.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // 使用 AndroidLogger 打印 Koin 日誌
            androidLogger(Level.INFO)
            // 注入 Android Context
            androidContext(this@App)
            // 載入網路層模組
            modules(networkModule)
        }
    }
}