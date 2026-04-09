package pl.deesoft.koinissue2379

import androidx.lifecycle.ViewModel
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

interface ScopedConnector {
    fun openScannerSession(): String
}

class ScopedConnectorImpl : ScopedConnector {
    override fun openScannerSession(): String = "Scoped dependency resolved from activityRetainedScope."
}

class ScannerInteractor(
    private val connector: ScopedConnector
) {
    fun startSession(): String = connector.openScannerSession()
}

class ScannerAutoConnectUseCase(
    private val scannerInteractor: ScannerInteractor,
) {
    operator fun invoke(): String = scannerInteractor.startSession()
}

class ScannerAutoConnectAndConfigureRangeUseCase(
    private val autoConnectUseCase: ScannerAutoConnectUseCase,
) {
    operator fun invoke(): String = autoConnectUseCase()
}

class MainViewModel(
    private val configureRangeUseCase: ScannerAutoConnectAndConfigureRangeUseCase
) : ViewModel() {
    fun renderMessage(): String = configureRangeUseCase()
}

val retainedScopeModule = module {
    activityRetainedScope {
        scoped<ScopedConnector> { ScopedConnectorImpl() }
    }
}

val appModule = module {
    factoryOf(::ScannerInteractor)
    factoryOf(::ScannerAutoConnectUseCase)
    factoryOf(::ScannerAutoConnectAndConfigureRangeUseCase)
    viewModelOf(::MainViewModel)
}
