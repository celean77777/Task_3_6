import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class ArrayClassTest {
    private ArrayClass arrayClass;

    @BeforeEach
    public void init(){
        arrayClass = new ArrayClass();
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void checkCreateArray1() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            arrayClass.createArray(new int[]{1, 5, 9, 3, 2});
        });
    }

    @Test
    public void checkCreateArray2(){
        try {
            arrayClass.createArray(new int[]{1, 5, 0, 3, 2});
            fail("test fail");
        }catch (Exception ignored){
        }
    }



    @ParameterizedTest
    @MethodSource("getDataForTest")
    public void checkCreateArray3(int[] input, int[] result) {
        Assertions.assertArrayEquals(result, arrayClass.createArray(input));
    }
    public static Stream<Arguments> getDataForTest(){
        List<Arguments> list = new ArrayList<>();
        list.add(Arguments.arguments(new int[] {1,2,4,5,6}, new int[] {5,6}));
        list.add(Arguments.arguments(new int[] {1,2,4,9,10}, new int[] {9,10}));
        list.add(Arguments.arguments(new int[] {1,2,5,5,4}, new int[] {}));
        list.add(Arguments.arguments(new int[] {4,2,4,5,6}, new int[] {5,6}));
        return list.stream();
    }

    @Test
    public void checkCheckArray(){
        Assertions.assertTrue(arrayClass.checkArray(new int[]{1,1,4,4,4,1,4}));
        Assertions.assertFalse(arrayClass.checkArray(new int[]{1,3,4,4,4,1}));
        Assertions.assertFalse(arrayClass.checkArray(new int[]{1,1,1,1}));
        Assertions.assertFalse(arrayClass.checkArray(new int[]{4,4,4,4}));
    }
}





