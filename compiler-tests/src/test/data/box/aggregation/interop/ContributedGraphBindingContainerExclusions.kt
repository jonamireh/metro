// WITH_ANVIL
// ENABLE_DAGGER_INTEROP

import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.MergeComponent
import dagger.Module

@ContributesSubcomponent(String::class, parentScope = AppScope::class, exclude = [IntBinding1::class])
interface StringGraph {
    val int: Int

    @ContributesSubcomponent.Factory
    @ContributesTo(AppScope::class)
    interface Factory {
        fun create(): StringGraph
    }
}

@MergeComponent(AppScope::class)
interface AppGraph

@ContributesTo(String::class)
@Module
object IntBinding1 {
  @Provides fun provideInt(): Int = 1
}

@ContributesTo(String::class)
@Module
object IntBinding2 {
  @Provides fun provideInt(): Int = 2
}

fun box(): String {
  val graph = createGraph<AppGraph>()
  val stringGraph = graph.asContribution<StringGraph.Factory>().create()
  assertEquals(2, stringGraph.int)
  return "OK"
}
