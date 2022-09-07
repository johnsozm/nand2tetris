package nand2tetris.vm;

import java.io.File;

/**
 * Class which manages the writing of the .asm output file.
 */
public class CodeWriter {
    /**
     * Constructs a new code writer instance.
     * @param _output A File object pointing to the desired output file.
     */
    public CodeWriter(File _output) {

    }

    /**
     * Sets the VM filename - should be used when a new VM file is opened.
     * @param _filename The new VM filename to use.
     */
    public void setFileName(String _filename) {

    }

    /**
     * Writes code corresponding to a stack arithmetic command.
     */
    public void writeArithmetic(String _command) {

    }

    /**
     * Writes code corresponding to a push or pop command.
     * @param _type The command type to write (must be C_PUSH or C_POP)
     * @param _segment The segment to push from or pop to
     * @param _index The index to use within the given segment
     */
    public void writePushPop(Parser.CommandType _type, String _segment, int _index) {

    }

    /**
     * Closes the output stream. Call when done.
     */
    public void close() {

    }
}
