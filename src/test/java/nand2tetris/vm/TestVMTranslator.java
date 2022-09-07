package nand2tetris.vm;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;

public class TestVMTranslator {
    @Test
    public void testBasicTest() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/BasicTest.vm"});
        assertTrue("Assembly code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/BasicTest.asm"), new File("src/test/java/nand2tetris/vm/BasicTest.asm.cmp")));
    }

    @Test
    public void testPointerTest() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/PointerTest.vm"});
        assertTrue("Assembly code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/PointerTest.asm"), new File("src/test/java/nand2tetris/vm/PointerTest.asm.cmp")));
    }

    @Test
    public void testSimpleAdd() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/SimpleAdd.vm"});
        assertTrue("Assembly code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/SimpleAdd.asm"), new File("src/test/java/nand2tetris/vm/SimpleAdd.asm.cmp")));
    }

    @Test
    public void testStackTest() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/StackTest.vm"});
        assertTrue("Assembly code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/StackTest.asm"), new File("src/test/java/nand2tetris/vm/StackTest.asm.cmp")));
    }

    @Test
    public void testStaticTest() throws IOException {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/StaticTest.vm"});
        assertTrue("Assembly code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/vm/StaticTest.asm"), new File("src/test/java/nand2tetris/vm/StaticTest.asm.cmp")));
    }
}
