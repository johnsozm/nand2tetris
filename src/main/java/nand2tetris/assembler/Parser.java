package nand2tetris.assembler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class which parses .asm files into machine code
 */
public class Parser {
    /**Array of lines in the file*/
    private ArrayList<String> lines;
    /**Line currently being parsed*/
    private int currentLine;
    /**Command type of the last parsed line*/
    private CommandType commandType;
    /**Destination of the last parsed line*/
    private String dest;
    /**Computation of the last parsed line*/
    private String comp;
    /**Jump instruction of the last parsed line*/
    private String jump;
    /**Symbol used on the last parsed line*/
    private String symbol;

    /**
     * Enum for command types
     */
    enum CommandType {
        /**Load constant command*/
        A_COMMAND,
        /**General command*/
        C_COMMAND,
        /**Label*/
        L_COMMAND
    }

    /**
     * Initializes parser for the given assembly file.
     *
     * @param _input The file to be read
     */
    public Parser(File _input) throws FileNotFoundException {
        currentLine = -1;
        lines = new ArrayList<>();

        //Read in all lines, trimming excess whitespace and comments
        Scanner scanner = new Scanner(_input);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            //Remove whitespace
            line = line.replaceAll("[ \t]+", "");

            //Remove comments if present
            int trimIndex = line.indexOf("//");
            if (trimIndex != -1) {
                line = line.substring(0, trimIndex);
            }

            if (!line.equals("")) {
                lines.add(line);
            }
        }
        scanner.close();
    }

    /**
     * Checks if the assembly file has more commands remaining.
     * @return True if there are more commands left, false if not.
     */
    public boolean hasMoreCommands() {
        return currentLine < lines.size() - 1;
    }

    /**
     * Advances parser to the next line of the assembly file and parses it.
     */
    public void advance() {
        currentLine++;
        String line = lines.get(currentLine);

        //Determine command type and parse variables as needed
        if (line.startsWith("@")) {
            commandType = CommandType.A_COMMAND;
            symbol = line.substring(1);
            dest = "";
            comp = "";
            jump = "";
        }
        else if (line.startsWith("(")) {
            commandType = CommandType.L_COMMAND;
            symbol = line.substring(1, line.length() - 1);
            dest = "";
            comp = "";
            jump = "";
        }
        else {
            commandType = CommandType.C_COMMAND;
            symbol = "";
            int equalsIndex = line.indexOf("=");
            int semicolonIndex = line.indexOf(";");

            //Parse dest
            if (equalsIndex == -1) {
                dest = "";
            }
            else {
                dest = line.substring(0, equalsIndex);
            }

            //Parse jump
            if (semicolonIndex == -1) {
                jump = "";
                semicolonIndex = line.length();
            }
            else {
                jump = line.substring(semicolonIndex + 1);
            }

            //Parse comp
            comp = line.substring(equalsIndex + 1, semicolonIndex);
        }
    }

    /**
     * Gets the type of the current command.
     *
     * @return A CommandType enum corresponding to the current command's type.
     */
    public CommandType commandType() {
        return commandType;
    }

    /**
     * Gets the symbol on the current line.
     * Should only be called for A_COMMAND or L_COMMAND lines.
     *
     * @return The symbol used on the current line.
     */
    public String symbol() {
        return symbol;
    }

    /**
     * Gets the destination mnemonic on the current line.
     * Should only be called for C_COMMAND lines.
     *
     * @return The destination mnemonic of the current line.
     */
    public String dest() {
        return dest;
    }

    /**
     * Gets the comparison mnemonic on the current line.
     * Should only be called for C_COMMAND lines.
     *
     * @return The comparison mnemonic of the current line.
     */
    public String comp() {
        return comp;
    }

    /**
     * Gets the jump mnemonic on the current line.
     * Should only be called for C_COMMAND lines.
     *
     * @return The jump mnemonic of the current line.
     */
    public String jump() {
        return jump;
    }
}
