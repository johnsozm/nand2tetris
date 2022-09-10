package nand2tetris.compiler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Primary compiler class. Compiles the .jack file(s) specified into .vm files.
 */
public class JackCompiler {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: JackCompiler [file or directory name]");
            System.exit(1);
        }

        File input = new File(args[0]);

        //Error if input path does not exist
        if (!input.exists()) {
            System.err.println("No such file or directory " + args[0]);
            System.exit(1);
        }

        File[] toParse = new File[]{input};

        if (input.isDirectory()) {
            FilenameFilter filter = (dir, name) -> name.toLowerCase().endsWith(".jack");
            toParse = input.listFiles(filter);
            if (toParse == null || toParse.length == 0) {
                System.err.println("No .jack files found in given directory");
                System.exit(1);
            }
        }

        for (File f: toParse) {
            try {
                JackTokenizer tokenizer = new JackTokenizer(f);
                File output = new File(f.getAbsolutePath().replaceAll("\\.jack$", ".vm"));
                CompilationEngine engine = new CompilationEngine(tokenizer, output);
                engine.compileClass();
            }
            catch (IOException e) {
                System.err.println("Error while writing output file.");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }
}
