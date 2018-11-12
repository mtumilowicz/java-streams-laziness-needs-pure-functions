import org.junit.Test;

import java.util.stream.IntStream;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mtumilowicz on 2018-11-12.
 */
public class StreamLazinessTest {
    
    @Test
    public void lazinessNeedsPureFunctions() {
        int[] arr = {0};

        IntStream integers = IntStream.iterate(0, integer -> ++integer);
        IntStream integersWhile = integers.takeWhile(x -> x < arr[0]);
        
        arr[0]=10;

        long count = integersWhile.count();

        assertThat(count, is(10L));
    }
}
