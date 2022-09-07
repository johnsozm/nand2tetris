package nand2tetris.assembler;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;

public class TestAssembler {
    @Test
    public void testAdd() throws IOException {
        Assembler.main(new String[] {"src/test/java/nand2tetris/assembler/Add.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/assembler/Add.hack"), new File("src/test/java/nand2tetris/assembler/Add.hack.cmp")));
    }

    @Test
    public void testMax() throws IOException {
        Assembler.main(new String[] {"src/test/java/nand2tetris/assembler/Max.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/assembler/Max.hack"), new File("src/test/java/nand2tetris/assembler/Max.hack.cmp")));
    }

    @Test
    public void testPong() throws IOException {
        Assembler.main(new String[] {"src/test/java/nand2tetris/assembler/Pong.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/assembler/Pong.hack"), new File("src/test/java/nand2tetris/assembler/Pong.hack.cmp")));
    }

    @Test
    public void testRect() throws IOException {
        Assembler.main(new String[] {"src/test/java/nand2tetris/assembler/Rect.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/java/nand2tetris/assembler/Rect.hack"), new File("src/test/java/nand2tetris/assembler/Rect.hack.cmp")));
    }
}