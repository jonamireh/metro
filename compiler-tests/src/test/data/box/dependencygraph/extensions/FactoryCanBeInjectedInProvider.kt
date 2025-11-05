@DependencyGraph(AppScope::class)
interface AppGraph {
    val loggedInGraph: LoggedInGraph
}

interface LoggedInScope

@SingleIn(LoggedInScope::class)
@GraphExtension(LoggedInScope::class)
interface LoggedInGraph {
    val loggedInFeatureInteractor: FeatureInteractor
}

interface FeatureScope

@SingleIn(FeatureScope::class)
@GraphExtension(FeatureScope::class)
interface FeatureGraph {
    @GraphExtension.Factory
    interface Factory {
        fun createFeatureGraph(): FeatureGraph
    }

    @ContributesTo(AppScope::class)
    interface ParentBindings {
        fun featureFactory(): Factory
    }
}

interface FeatureInteractor

@ContributesTo(LoggedInScope::class)
interface LoggedInFeatureModule {
    companion object {
        @Provides
        @SingleIn(LoggedInScope::class)
        fun provideLoggedInFeatureInteractor(
            factory: FeatureGraph.Factory
        ): FeatureInteractor = object : FeatureInteractor {}.also {
            factory.createFeatureGraph()
        }
    }
}

fun box(): String {
    val appGraph = createGraph<AppGraph>()
    assertNotNull(appGraph)
    return "OK"
}