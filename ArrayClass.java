
import java.util.Arrays;

public class ArrayClass {



    public int[] createArray (int [] inputArray) throws RuntimeException {
            Integer lastDigit = null;
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i] == 4) {
                    lastDigit = i;
                }
            }
            return Arrays.copyOfRange(inputArray, lastDigit + 1, inputArray.length);
    }


    public boolean checkArray (int [] inputArray) {
        Boolean bol1 = Arrays.stream(inputArray).allMatch(s->s==1||s==4);
        Boolean bol2 = Arrays.stream(inputArray).anyMatch(s->s==1);
        Boolean bol3 = Arrays.stream(inputArray).anyMatch(s->s==4);
        return bol1&bol2&bol3;
    }


}
