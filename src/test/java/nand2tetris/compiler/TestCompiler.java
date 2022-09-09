package nand2tetris.compiler;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;


public class TestCompiler {
    @Test
    public void testArrayTest() throws IOException {
        JackCompiler.main(new String[]{"src/test/java/nand2tetris/compiler/ArrayTest"});
        assertTrue("VM code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/compiler/ArrayTest/Main.vm"), new File("src/test/java/nand2tetris/compiler/ArrayTest/MainT.cmp")));
    }

    @Test
    public void testSquare() throws IOException {
        JackCompiler.main(new String[]{"src/test/java/nand2tetris/compiler/Square"});
        assertTrue("VM code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/compiler/Square/Main.vm"), new File("src/test/java/nand2tetris/compiler/Square/MainT.cmp")));
        assertTrue("VM code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/compiler/Square/Square.vm"), new File("src/test/java/nand2tetris/compiler/Square/SquareT.cmp")));
        assertTrue("VM code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/compiler/Square/SquareGame.vm"), new File("src/test/java/nand2tetris/compiler/Square/SquareGameT.cmp")));
    }
}
