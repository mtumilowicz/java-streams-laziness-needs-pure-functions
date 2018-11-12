# java-streams-laziness-needs-pure-functions
Example why java streams laziness needs pure functions.

_Reference_: https://www.youtube.com/watch?v=8srdPlPfFIk

# preface

# project description
We provide simple example in `StreamLazinessTest`:
```
int[] arr = {0};

IntStream integers = IntStream.iterate(0, integer -> ++integer);
IntStream integersWhile = integers.takeWhile(x -> x < arr[0]);

arr[0]=10;

long count = integersWhile.count();

assertThat(count, is(10L));
```
* `integersWhile` when consumed (before `arr[0]=10`) when consumed
would return `assertThat(count, is(0L));`, cause `x -> x < arr[0]`
would be false (`arr = {0}`) so after changing the value we have
unexpected result (suppose that other thread change the value).