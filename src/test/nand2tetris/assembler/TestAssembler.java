package nand2tetris.assembler;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;

public class TestAssembler {
    @Test
    public void testAdd() throws IOException {
        Assembler.main(new String[] {"src/test/Add.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/Add.hack"), new File("src/test/Add.hack.cmp")));
    }

    @Test
    public void testMax() throws IOException {
        Assembler.main(new String[] {"src/test/Max.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/Max.hack"), new File("src/test/Max.hack.cmp")));
    }

    @Test
    public void testPong() throws IOException {
        Assembler.main(new String[] {"src/test/Pong.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/Pong.hack"), new File("src/test/Pong.hack.cmp")));
    }

    @Test
    public void testRect() throws IOException {
        Assembler.main(new String[] {"src/test/Rect.asm"});
        assertTrue("Machine code does not match", FileUtils.contentEquals(new File("src/test/Rect.hack"), new File("src/test/Rect.hack.cmp")));
    }
}