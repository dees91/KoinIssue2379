# Koin retained-scope regression repro

This project is a minimal Android repro for the Koin regression tracked in [InsertKoinIO/koin#2379](https://github.com/InsertKoinIO/koin/issues/2379).

The app creates:

- an `activityRetainedScope`
- a `scoped` `ScopedConnector`
- a root `viewModelOf(::MainViewModel)`
- a short factory chain:
  `MainViewModel -> ScannerAutoConnectAndConfigureRangeUseCase -> ScannerAutoConnectUseCase -> ScannerInteractor -> ScopedConnector`

`MainActivity` resolves the `ViewModel` with `by viewModel()` from an `AndroidScopeComponent` activity that owns the retained scope.

## Expected behavior

### Koin 4.2.0

This repo defaults to `4.2.0` in [gradle/libs.versions.toml](gradle/libs.versions.toml).

Expected result after launching the app:

- the app crashes during startup
- the top cause is a `NoDefinitionFoundException`
- Koin reports that `ScopedConnector` is being resolved on `_root_`

### Koin 4.1.1

Change only this line in [gradle/libs.versions.toml](gradle/libs.versions.toml):

```toml
koin = "4.1.1"
```

Expected result after rebuilding and launching:

- no crash
- the screen shows `Scoped dependency resolved from activityRetainedScope.`

### Koin 4.2.1-RC1

Change the same line to:

```toml
koin = "4.2.1-RC1"
```

Expected result after rebuilding and launching:

- crash again during startup
- same class of failure as `4.2.0`

## Notes

- No code changes are required between versions; only the Koin version changes.
