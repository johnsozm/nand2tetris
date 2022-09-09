package nand2tetris.compiler;

import org.junit.Test;

import java.io.File;
import java.io.IOException;


public class TestCompiler {
    @Test
    public void compileAll() throws IOException {
        JackCompiler.main(new String[]{"src/test/java/nand2tetris/compiler/"});
    }
}
