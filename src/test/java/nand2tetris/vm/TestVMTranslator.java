package nand2tetris.vm;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;

public class TestVMTranslator {
    @Test
    public void testFibonacciElement() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/FibonacciElement"});
        //TODO: Compare to known-good file
    }

    @Test
    public void testNestedCall() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/NestedCall"});
        //TODO: Compare to known-good file
    }

    @Test
    public void testStaticsTest() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/StaticsTest"});
        //TODO: Compare to known-good file
    }
}
