package nand2tetris.vm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class which manages the writing of the .asm output file.
 */
public class CodeWriter {
    /**Output file writer*/
    private FileWriter writer;
    /**Current VM filename*/
    private String filename;
    /**Numeric label for conditional branching*/
    private int branchNumber;

    /**
     * Constructs a new code writer instance.
     * @param _output A File object pointing to the desired output file.
     */
    public CodeWriter(File _output) throws IOException {
        writer = new FileWriter(_output);
        branchNumber = 1;
    }

    /**
     * Sets the VM filename - should be used when a new VM file is opened.
     * @param _filename The new VM filename to use.
     */
    public void setFileName(String _filename) {
        filename = _filename.replace("\\.vm$", "");
    }

    /**
     * Writes code corresponding to a stack arithmetic command.
     */
    public void writeArithmetic(String _command) throws IOException {
        //Special handling for unary operators
        if (_command.equals("neg") || _command.equals("not")) {
            writer.write("@SP\n");
            writer.write("A=M-1\n");
            if (_command.equals("neg")) {
                writer.write("M=-M\n");
            }
            else {
                writer.write("M=!M\n");
            }
            return;
        }

        //Decrement stack pointer
        writer.write("@SP\n");
        writer.write("M=M-1\n");
        //Store y in D register
        writer.write("A=M\n");
        writer.write("D=M\n");
        //Decrement address so that x is in M register
        writer.write("A=A-1\n");

        switch (_command) {
            case "add":
                writer.write("M=M+D\n");
                break;
            case "sub":
                writer.write("M=M-D\n");
                break;
            case "and":
                writer.write("M=M&D\n");
                break;
            case "or":
                writer.write("M=M|D\n");
                break;
            case "eq":
            case "gt":
            case "lt":
                writer.write("D=M-D\n");
                writer.write("@branch" + branchNumber + "\n");
                writer.write("D;J" + _command.toUpperCase() + "\n");
                writer.write("D=0\n");
                writer.write("@endbranch" + branchNumber + "\n");
                writer.write("0;JMP\n");
                writer.write("(branch" + branchNumber + ")\n");
                writer.write("D=-1\n");
                writer.write("(endbranch" + branchNumber + ")\n");
                writer.write("@SP\n");
                writer.write("A=M-1\n");
                writer.write("M=D\n");
                branchNumber++;
                break;
        }
    }

    /**
     * Writes code corresponding to a push or pop command.
     * @param _type The command type to write (must be C_PUSH or C_POP)
     * @param _segment The segment to push from or pop to
     * @param _index The index to use within the given segment
     */
    public void writePushPop(Parser.CommandType _type, String _segment, int _index) throws IOException {
        //Special handling for constant segment
        if (_segment.equals("constant")) {
            if (_type == Parser.CommandType.C_PUSH) {
                //Load constant to D register
                writer.write("@" + _index + "\n");
                writer.write("D=A\n");
                //Jump to stack head and write
                writer.write("@SP\n");
                writer.write("A=M\n");
                writer.write("M=D\n");
                //Increment stack pointer
                writer.write("@SP\n");
                writer.write("M=M+1\n");
            }
            //No logic needed for C_POP - should do nothing if called this way for some reason
            return;
        }

        //Calculate segment address and store in D register
        switch (_segment) {
            case "local":
                writer.write("@" + _index + "\n");
                writer.write("D=A\n");
                writer.write("@LCL\n");
                writer.write("D=D+M\n");
                break;
            case "argument":
                writer.write("@" + _index + "\n");
                writer.write("D=A\n");
                writer.write("@ARG\n");
                writer.write("D=D+M\n");
                break;
            case "this":
                writer.write("@" + _index + "\n");
                writer.write("D=A\n");
                writer.write("@THIS\n");
                writer.write("D=D+M\n");
                break;
            case "that":
                writer.write("@" + _index + "\n");
                writer.write("D=A\n");
                writer.write("@THAT\n");
                writer.write("D=D+M\n");
                break;
            case "pointer":
                writer.write("@" + (_index + 3) + "\n");
                writer.write("D=A\n");
                break;
            case "temp":
                writer.write("@" + (_index + 5) + "\n");
                writer.write("D=A\n");
                break;
            case "static":
                writer.write("@" + filename + "." + _index + "\n");
                writer.write("D=A\n");
                break;
        }

        //Write push/pop logic
        if (_type == Parser.CommandType.C_PUSH) {
            //Fetch value
            writer.write("A=D\n");
            writer.write("D=M\n");
            //Put on top of stack
            writer.write("@SP\n");
            writer.write("A=M\n");
            writer.write("M=D\n");
            //Increment stack pointer
            writer.write("@SP\n");
            writer.write("M=M+1\n");
        }
        else if (_type == Parser.CommandType.C_POP) {
            //Put storage address at M[13]
            writer.write("@13\n");
            writer.write("M=D\n");
            //Fetch top value from stack and store in D register
            writer.write("@SP\n");
            writer.write("A=M-1\n");
            writer.write("D=M\n");
            //Fetch address from M[13] and store value
            writer.write("@13\n");
            writer.write("A=M\n");
            writer.write("M=D\n");
            //Decrement stack pointer
            writer.write("@SP\n");
            writer.write("M=M-1\n");
        }
    }

    /**
     * Closes the output stream. Call when done.
     */
    public void close() throws IOException {
        writer.close();
    }
}
