package nand2tetris.vm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class which parses commands from .vm files.
 */
public class Parser {
    /**Array of lines in the file*/
    private ArrayList<String> lines;
    /**Line currently being parsed*/
    private int currentLine;
    /**Command type of the last parsed line*/
    private CommandType commandType;
    /**First argument of the last parsed line*/
    private String arg1;
    /**Second argument of the last parsed line*/
    private int arg2;

    /**
     * Enum for command types
     */
    enum CommandType {
        /**Stack arithmetic command*/
        C_ARITHMETIC,
        /**Push command*/
        C_PUSH,
        /**Pop command*/
        C_POP,
        /**Label*/
        C_LABEL,
        /**Goto command*/
        C_GOTO,
        /**Conditional goto command*/
        C_IF,
        /**Function definition*/
        C_FUNCTION,
        /**Return command*/
        C_RETURN,
        /**Function call command*/
        C_CALL
    }

    /**
     * Constructs a new parser using the given input file.
     * @param _input A file object pointing to the input file.
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
     * Checks if the parser has more commands left to parse.
     * @return True if there are more commands, false if not.
     */
    public boolean hasMoreCommands() {
        return currentLine < lines.size() - 1;
    }

    /**
     * Advances the parser to the next command and parses it.
     */
    public void advance() {
        currentLine++;
        String[] tokens = lines.get(currentLine).split(" ");

        //Parse arguments
        if (tokens.length > 1) {
            arg1 = tokens[1];
        }
        else {
            arg1 = "";
        }

        if (tokens.length == 3) {
            try {
                arg2 = Integer.parseInt(tokens[2]);
            }
            catch (NumberFormatException e) {
                arg2 = 0;
            }
        }
        else {
            arg2 = 0;
        }

        //Determine command type
        switch (tokens[0]) {
            case "push":
                commandType = CommandType.C_PUSH;
                break;
            case "pop":
                commandType = CommandType.C_POP;
                break;
            case "label":
                commandType = CommandType.C_LABEL;
                break;
            case "goto":
                commandType = CommandType.C_GOTO;
                break;
            case "if-goto":
                commandType = CommandType.C_IF;
                break;
            case "function":
                commandType = CommandType.C_FUNCTION;
                break;
            case "call":
                commandType = CommandType.C_CALL;
                break;
            case "return":
                commandType = CommandType.C_RETURN;
                break;
            default:
                commandType = CommandType.C_ARITHMETIC;
        }
    }

    /**
     * Gets the current command type.
     * @return A CommandType enum representing the current command type.
     */
    public CommandType commandType() {
        return commandType;
    }

    /**
     * Returns the first argument of the current command.
     * @return The first argument, as a string.
     */
    public String arg1() {
        return arg1;
    }

    /**
     * Returns the second argument of the current command.
     * @return The second argument, as a string.
     */
    public int arg2() {
        return arg2;
    }
}
