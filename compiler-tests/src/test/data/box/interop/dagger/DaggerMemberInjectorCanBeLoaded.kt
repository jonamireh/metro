// ENABLE_DAGGER_KSP
// ENABLE_DAGGER_INTEROP

// MODULE: lib
// FILE: Dependency.java
public interface Dependency {
}

// FILE: ExampleClass.java
import javax.inject.Inject;

public class ExampleClass {
    @Inject public Dependency dependency;
}

// MODULE: main(lib)
// FILE: DependencyImpl.kt
@ContributesBinding(AppScope::class)
class DependencyImpl @Inject constructor() : Dependency

// FILE: ExampleInjector.kt
@ContributesTo(AppScope::class)
interface ExampleInjector {
  fun inject(example: ExampleClass)
}

// FILE: ExampleGraph.kt
@DependencyGraph(AppScope::class)
interface ExampleGraph

fun box(): String {
  val graph = createGraph<ExampleGraph>()
  val example = ExampleClass()

  graph.inject(example)
  assertNotNull(example.dependency)
  return "OK"
}