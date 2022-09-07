package nand2tetris.vm;

import java.io.File;

/**
 * Class which parses commands from .vm files.
 */
public class Parser {
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
    public Parser(File _input) {

    }

    /**
     * Checks if the parser has more commands left to parse.
     * @return True if there are more commands, false if not.
     */
    public boolean hasMoreCommands() {
        return false;
    }

    /**
     * Advances the parser to the next command and parses it.
     */
    public void advance() {

    }

    /**
     * Gets the current command type.
     * @return A CommandType enum representing the current command type.
     */
    public CommandType commandType() {
        return null;
    }

    /**
     * Returns the first argument of the current command.
     * @return The first argument, as a string.
     */
    public String arg1() {
        return "";
    }

    /**
     * Returns the second argument of the current command.
     * @return The second argument, as a string.
     */
    public String arg2() {
        return "";
    }
}
