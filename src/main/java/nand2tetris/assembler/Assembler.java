package nand2tetris.assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Primary assembler class. Assembles the .asm file specified on the command line into a .hack file.
 */
public class Assembler {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Assembler.jar [filename]");
            return;
        }

        //Throw error if input file does not exist
        String assemblyFilename = args[0];
        File assemblyFile = new File(assemblyFilename);
        if (!assemblyFile.exists()) {
            System.out.println("Error: No such file " + assemblyFilename);
            return;
        }

        //Generate output filename based on input filename
        String hackFilename;
        File hackFile;
        if (!assemblyFilename.endsWith(".asm")) {
            System.out.println("Error: Assembler files should use the .asm extension.");
            return;
        }
        else {
            hackFilename = assemblyFilename.substring(0, assemblyFilename.length() - 3) + "hack";
            hackFile = new File(hackFilename);
        }

        //Pass 1: Populate symbol table with jump labels
        SymbolTable symbols = new SymbolTable();
        Parser parser;
        int commandLocation = -1;
        try {
            parser = new Parser(assemblyFile);
            while (parser.hasMoreCommands()) {
                parser.advance();
                if (parser.commandType() == Parser.CommandType.L_COMMAND) {
                    symbols.addEntry(parser.symbol(), commandLocation + 1);
                }
                else {
                    commandLocation++;
                }
            }
        }
        catch (FileNotFoundException ignored) {} //Check for file existence is above

        //Pass 2: Translate file
        try {
            FileWriter writer = new FileWriter(hackFile);
            int nextMemorySlot = 16;
            try {
                parser = new Parser(assemblyFile);
                while (parser.hasMoreCommands()) {
                    parser.advance();
                    switch (parser.commandType()) {
                        case A_COMMAND:
                            int value;

                            //Check for literal integer load
                            try {
                                value = Integer.parseInt(parser.symbol());
                            }

                            //Check against symbol table, add if needed
                            catch (NumberFormatException e) {
                                if (symbols.contains(parser.symbol())) {
                                    value = symbols.getAddress(parser.symbol());
                                }
                                else {
                                    symbols.addEntry(parser.symbol(), nextMemorySlot);
                                    value = nextMemorySlot;
                                    nextMemorySlot++;
                                }
                            }

                            //Translate load value to binary and write load command
                            String binaryString = Integer.toString(value, 2);
                            binaryString = "0".repeat(15 - binaryString.length()) + binaryString;
                            writer.write("0" + binaryString + "\n");
                            break;

                        case C_COMMAND:
                            String comp = Code.comp(parser.comp());
                            String dest = Code.dest(parser.dest());
                            String jump = Code.jump(parser.jump());
                            writer.write("111" + comp + dest + jump + "\n");
                            break;

                        case L_COMMAND:
                            break;
                    }
                }

                writer.close();
            }
            catch (FileNotFoundException ignored) {} //Check for file existence is above
        }
        catch (IOException e) {
            System.out.println("IOException while trying to write .hack file.");
            System.out.println(e.getMessage());
        }
    }
}
