// ENABLE_DAGGER_INTEROP

import java.util.Optional
import kotlin.jvm.optionals.getOrDefault

interface LoggedInScope
interface FeatureScope

interface DelegateDependency

@ContributesBinding(AppScope::class)
class DelegateDependencyImpl @Inject constructor(
    private val appDependency: AppDependency,
    private val LoggedInDependency: Optional<LoggedInDependency>
): DelegateDependency by LoggedInDependency.getOrDefault(appDependency)

interface AppDependency : DelegateDependency

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class AppDependencyImpl @Inject constructor(): AppDependency

interface LoggedInDependency : DelegateDependency

@ContributesBinding(LoggedInScope::class)
@SingleIn(LoggedInScope::class)
class LoggedInDependencyImpl @Inject constructor(): LoggedInDependency

@dagger.Module
@ContributesTo(AppScope::class)
interface DependencyModule {
    @dagger.BindsOptionalOf
    fun provideOptional(): LoggedInDependency
}

@SingleIn(FeatureScope::class)
@GraphExtension(FeatureScope::class)
interface FeatureGraph {
    val dependency: DelegateDependency
}

@SingleIn(LoggedInScope::class)
@GraphExtension(LoggedInScope::class)
interface LoggedInGraph {
    val featureGraph: FeatureGraph
}

@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
interface AppGraph {
    val loggedInGraph: LoggedInGraph
}

fun box(): String {
    val graph = createGraph<AppGraph>()
    assertNotNull(graph.loggedInGraph.featureGraph.dependency)
    return "OK"
}