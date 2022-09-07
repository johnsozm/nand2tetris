package nand2tetris.assembler;
import java.io.File;

/**
 * Class which parses .asm files into machine code
 */
public class Parser {
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
    public Parser(File _input) {

    }

    /**
     * Advances parser to the next line of the assembly file.
     */
    public void advance() {

    }

    /**
     * Gets the type of the current command.
     *
     * @return A CommandType enum corresponding to the current command's type.
     */
    public CommandType commandType() {
        return null;
    }

    /**
     * Gets the symbol on the current line.
     * Should only be called for A_COMMAND or L_COMMAND lines.
     *
     * @return The symbol used on the current line.
     */
    public String symbol() {
        return null;
    }

    /**
     * Gets the destination mnemonic on the current line.
     * Should only be called for C_COMMAND lines.
     *
     * @return The destination mnemonic of the current line.
     */
    public String dest() {
        return null;
    }

    /**
     * Gets the comparison mnemonic on the current line.
     * Should only be called for C_COMMAND lines.
     *
     * @return The comparison mnemonic of the current line.
     */
    public String comp() {
        return null;
    }

    /**
     * Gets the jump mnemonic on the current line.
     * Should only be called for C_COMMAND lines.
     *
     * @return The jump mnemonic of the current line.
     */
    public String jump() {
        return null;
    }
}
