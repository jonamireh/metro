// MODULE: lib
// ENABLE_DAGGER_KSP
// DISABLE_METRO

// FILE: Dependency.java
public interface Dependency {
}

// FILE: ExampleClass.java
import javax.inject.Inject;
import javax.inject.Named;

public class ExampleClass {
  @Inject @Named("dependency") public Dependency dependency;
  Dependency setterDep = null;
  Dependency setterDep2 = null;
  String setterDep3 = null;

  // Setter injection
  @Inject public void setterInject(@Named("dependency") Dependency dep) {
    this.setterDep = dep;
  }

  // Setter injection
  @Inject public void setterInject2(@Named("dependency") Dependency dep, String stringDep) {
    this.setterDep2 = dep;
    this.setterDep3 = stringDep;
  }
}

// MODULE: main(lib)
// ENABLE_DAGGER_INTEROP

// FILE: DependencyImpl.kt
class DependencyImpl @Inject constructor() : Dependency

// FILE: ExampleInjector.kt
@ContributesTo(AppScope::class)
interface ExampleInjector {
  fun inject(example: ExampleClass)
}

// FILE: ExampleGraph.kt
import javax.inject.Named

@DependencyGraph(AppScope::class)
interface ExampleGraph {
  @Provides fun provideString(): String = "Hello"
  @Binds @Named("dependency") fun DependencyImpl.bind(): Dependency
}

fun box(): String {
  val graph = createGraph<ExampleGraph>()
  val example = ExampleClass()

  graph.inject(example)
  assertNotNull(example.dependency)
  assertNotNull(example.setterDep)
  assertNotNull(example.setterDep2)
  assertEquals("Hello", example.setterDep3)
  return "OK"
}