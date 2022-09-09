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
        assertTrue("Assembler code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/FibonacciElement/FibonacciElement.asm"), new File("src/test/java/nand2tetris/vm/FibonacciElement/FibonacciElement.asm.cmp")));
    }

    @Test
    public void testNestedCall() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/NestedCall"});
        assertTrue("Assembler code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/NestedCall/NestedCall.asm"), new File("src/test/java/nand2tetris/vm/NestedCall/NestedCall.asm.cmp")));

    }

    @Test
    public void testStaticsTest() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/StaticsTest"});
        assertTrue("Assembler code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/StaticsTest/StaticsTest.asm"), new File("src/test/java/nand2tetris/vm/StaticsTest/StaticsTest.asm.cmp")));

    }
}
