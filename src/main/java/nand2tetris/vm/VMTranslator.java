package nand2tetris.vm;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Primary VM translator class. Translates the .vm file or directory specified on the command line into a .asm file.
 */
public class VMTranslator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: VMTranslator.jar [file or directory name]");
            System.exit(1);
        }

        File input = new File(args[0]);

        //Error if input path does not exist
        if (!input.exists()) {
            System.err.println("No such file or directory " + args[0]);
            System.exit(1);
        }

        File output;
        File[] toParse = new File[]{input};

        if (input.isDirectory()) {
            FilenameFilter filter = (dir, name) -> name.toLowerCase().endsWith(".vm");
            toParse = input.listFiles(filter);
            if (toParse == null || toParse.length == 0) {
                System.err.println("No .vm files found in given directory");
                System.exit(1);
            }
            output = new File(args[0] + "/" + input.getName() + ".asm");
        }
        else {
            if (!args[0].endsWith(".vm")) {
                System.err.println("Error: VM files should use the .vm extension.");
                System.exit(1);
            }
            String outPath = args[0].replaceAll("\\.vm$", ".asm");
            output = new File(outPath);
        }

        try {
            CodeWriter writer = new CodeWriter(output);
            for (File f: toParse) {
                writer.setFileName(f.getName());
                Parser parser = new Parser(f);

                while (parser.hasMoreCommands()) {
                    parser.advance();
                    switch (parser.commandType()) {
                        case C_ARITHMETIC:
                            writer.writeArithmetic(parser.arg1());
                            break;
                        case C_PUSH:
                        case C_POP:
                            writer.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                            break;
                    }
                }
            }
            writer.close();
        }
        catch (IOException e) {
            System.err.println("IOException while trying to write .asm file.");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
