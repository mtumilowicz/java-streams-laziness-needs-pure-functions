# java-streams-laziness-needs-pure-functions
Example why java streams laziness needs pure functions.

_Reference_: https://www.youtube.com/watch?v=8srdPlPfFIk  
_Reference_: https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html

# preface
## streams laziness, intermediary vs terminal operations
Stream operations are divided into intermediate (Stream-producing) 
operations and terminal (value- or side-effect-producing) operations. 
Intermediate operations are always lazy.

Intermediate operations return a new stream. They are always lazy; 
executing an intermediate operation such as `filter()` does not actually 
perform any filtering, but instead creates a new stream that, when 
traversed, contains the elements of the initial stream that match the 
given predicate. Traversal of the pipeline source does not begin until 
the terminal operation of the pipeline is executed.

Intermediate operations are further divided into stateless and stateful 
operations. Stateless operations, such as filter and map, retain no 
state from previously seen element when processing a new element -- 
each element can be processed independently of operations on other 
elements. Stateful operations, such as distinct and sorted, may 
incorporate state from previously seen elements when processing new 
elements.

**Pipelines containing exclusively stateless intermediate operations can 
be processed in a single pass, whether sequential or parallel, with 
minimal data buffering.**

Couple of **terminal operations**:
* `Optional<T> min/max(Comparator<? super T> comparator)`
* `boolean allMatch/anyMatch/noneMatch (Predicate<? super T> predicate)`
* `void forEach(Consumer<? super T> action)`
* `Optional<T> reduce(BinaryOperator<T> accumulator)`
* `T reduce(T identity, BinaryOperator<T> accumulator)`

Couple of **intermediary  operations**:
* `<R> Stream<R> map(Function<? super T, ? extends R> mapper)`
* `Stream<T> sorted() // if T is not comparable – runtime exception`
* `Stream<T> sorted(Comparator<? super T> comparator)`
* `Stream<T> peek(Consumer<? super T> action)`

## effectively final
A non final local variable or method parameter whose value is never 
changed after initialization is known as effectively final.

Variables used in the **lambda expressions** must be final or 
effectively final.

## pure functions
Function to be pure, has to follow two rules:
* it does not change anything
* it does not depend on anything that changes

# project description
We provide simple example in `StreamLazinessTest`:
```
int[] arr = {0};

IntStream integers = IntStream.iterate(0, integer -> ++integer);
IntStream integersWhile = integers.takeWhile(x -> x < arr[0]); // takeWhile is intermediate

arr[0]=10;

long count = integersWhile.count();

assertThat(count, is(10L));
```
* `integersWhile` when consumed (before `arr[0]=10`) when consumed
would return `assertThat(count, is(0L));`, cause `x -> x < arr[0]`
would be false (`arr = {0}`) so after changing the value we have
unexpected result (suppose that other thread change the value).

# remark
Note that java to a certain degree requires pure functions:
```
int value = 0;

IntStream integers = IntStream.iterate(0, integer -> ++integer);
IntStream integersWhile = integers.takeWhile(x -> x < value); // will not compile, cause: Variable used in lambda expression should be final or effectively final

value=10;

long count = integersWhile.count();

assertThat(count, is(10L));
```