package nand2tetris.vm;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;

public class TestVMTranslator {
    @Test
    void testBasicTest() {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/BasicTest.vm"});
        //TODO: Add assertion once I have comparison files
    }

    @Test
    void testPointerTest() {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/PointerTest.vm"});
        //TODO: Add assertion once I have comparison files
    }

    @Test
    void testSimpleAdd() {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/SimpleAdd.vm"});
        //TODO: Add assertion once I have comparison files
    }

    @Test
    void testStackTest() {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/StackTest.vm"});
        //TODO: Add assertion once I have comparison files
    }

    @Test
    void testStaticTest() {
        VMTranslator.main(new String[]{"src/test/java/nand2tetris/vm/StaticTest.vm"});
        //TODO: Add assertion once I have comparison files
    }
}
