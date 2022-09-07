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
    /**Current function name (used as label prefix)*/
    private String currentFunction;
    /**Numeric label for return addresses*/
    private int returnNumber;

    /**
     * Constructs a new code writer instance.
     * @param _output A File object pointing to the desired output file.
     */
    public CodeWriter(File _output) throws IOException {
        writer = new FileWriter(_output);
        branchNumber = 1;
        currentFunction = "Sys.init";
        returnNumber = 1;
    }

    /**
     * Sets the VM filename - should be used when a new VM file is opened.
     *
     * @param _filename The new VM filename to use.
     */
    public void setFileName(String _filename) {
        filename = _filename.replace("\\.vm$", "");
    }

    /**
     * Writes code corresponding to a stack arithmetic command.
     *
     * @param _command The arithmetic command to be written.
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
     *
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
     * Writes initialization code.
     */
    public void writeInit() throws IOException {
        //Write SP=256
        writer.write("@256\n");
        writer.write("D=A\n");
        writer.write("@SP\n");
        writer.write("M=D\n");
        //Write jump to init method (no stack frame needed for initial call)
        writer.write("@Sys.init\n");
        writer.write("0;JMP\n");
    }

    /**
     * Writes assembly code corresponding to a label command.
     *
     * @param _label The label name to use.
     */
    public void writeLabel(String _label) throws IOException {
        writer.write("(" + currentFunction + "$" + _label + ")\n");
    }

    /**
     * Writes assembly code corresponding to a goto command.
     *
     * @param _label The label to be jumped to.
     */
    public void writeGoto(String _label) throws IOException {
        //Unconditional jump to the target label
        writer.write("@" + currentFunction + "$" + _label + "\n");
        writer.write("0;JMP\n");
    }

    /**
     * Writes assembly code corresponding to a conditional goto command.
     *
     * @param _label The label to be jumped to.
     */
    public void writeIf(String _label) throws IOException {
        //Decrement stack pointer and store popped value in D register
        writer.write("@SP\n");
        writer.write("M=M-1\n");
        writer.write("A=M\n");
        writer.write("D=M\n");
        //Conditional jump to target label
        writer.write("@" + currentFunction + "$" + _label + "\n");
        writer.write("D;JNE\n");
    }

    /**
     * Writes assembly code corresponding to a function call.
     *
     * @param _functionName The name of the function to be called.
     * @param _numArgs The number of arguments passed to the function.
     */
    public void writeCall(String _functionName, int _numArgs) throws IOException {
        //Push return address
        writer.write("@return" + returnNumber + "\n");
        writer.write("D=A\n");
        writer.write("@SP\n");
        writer.write("M=M+1\n");
        writer.write("A=M-1\n");
        writer.write("M=D\n");
        //Push LCL
        writer.write("@LCL\n");
        writer.write("D=A\n");
        writer.write("@SP\n");
        writer.write("M=M+1\n");
        writer.write("A=M-1\n");
        writer.write("M=D\n");
        //Push ARG
        writer.write("@ARG\n");
        writer.write("D=A\n");
        writer.write("@SP\n");
        writer.write("M=M+1\n");
        writer.write("A=M-1\n");
        writer.write("M=D\n");
        //Push THIS
        writer.write("@THIS\n");
        writer.write("D=A\n");
        writer.write("@SP\n");
        writer.write("M=M+1\n");
        writer.write("A=M-1\n");
        writer.write("M=D\n");
        //Push THAT
        writer.write("@THAT\n");
        writer.write("D=A\n");
        writer.write("@SP\n");
        writer.write("M=M+1\n");
        writer.write("A=M-1\n");
        writer.write("M=D\n");
        //Set ARG to base of argument stack
        writer.write("@" + (_numArgs + 5) + "\n");
        writer.write("D=A\n");
        writer.write("@SP\n");
        writer.write("D=A-D\n");
        writer.write("@ARG\n");
        writer.write("M=D\n");
        //LCL=SP
        writer.write("@SP\n");
        writer.write("D=M\n");
        writer.write("@LCL\n");
        writer.write("M=D\n");
        //Jump to function
        writer.write("@" + _functionName + "\n");
        writer.write("0;JMP\n");
        //Return address to jump back to when done
        writer.write("(return" + returnNumber + ")\n");
        returnNumber++;
    }

    /**
     * Writes assembly code corresponding to a return statement.
     */
    public void writeReturn() throws IOException {
        //Store LCL at M[14] for reference
        writer.write("@LCL\n");
        writer.write("D=M\n");
        writer.write("@14\n");
        writer.write("M=D\n");
        //Store return address at M[15] for later use
        writer.write("@5\n");
        writer.write("A=D-A\n");
        writer.write("D=M\n");
        writer.write("@15\n");
        writer.write("M=D\n");
        //Store return value at ARG
        writer.write("@SP\n");
        writer.write("A=M-1\n");
        writer.write("D=M\n");
        writer.write("@ARG\n");
        writer.write("A=M\n");
        writer.write("M=D\n");
        //Reposition stack pointer to ARG + 1
        writer.write("@ARG\n");
        writer.write("D=M+1\n");
        writer.write("@SP\n");
        writer.write("M=D\n");
        //Restore previous value of THAT
        writer.write("@14\n");
        writer.write("A=M-1\n");
        writer.write("D=M\n");
        writer.write("@THAT\n");
        writer.write("M=D\n");
        //Restore previous value of THIS
        writer.write("@14\n");
        writer.write("D=M\n");
        writer.write("@2\n");
        writer.write("A=D-A\n");
        writer.write("D=M\n");
        writer.write("@THIS\n");
        writer.write("M=D\n");
        //Restore previous value of ARG
        writer.write("@14\n");
        writer.write("D=M\n");
        writer.write("@3\n");
        writer.write("A=D-A\n");
        writer.write("D=M\n");
        writer.write("@ARG\n");
        writer.write("M=D\n");
        //Restore previous value of LCL
        writer.write("@14\n");
        writer.write("D=M\n");
        writer.write("@4\n");
        writer.write("A=D-A\n");
        writer.write("D=M\n");
        writer.write("@LCL\n");
        writer.write("M=D\n");
        //Fetch return address and jump to it
        writer.write("@15\n");
        writer.write("A=M\n");
        writer.write("0; JMP\n");
    }

    /**
     * Writes assembly code corresponding to a function declaration.
     *
     * @param _functionName The name of the function to declare.
     * @param _numLocals The number of local variables the function uses.
     */
    public void writeFunction(String _functionName, int _numLocals) throws IOException {
        writer.write("(" + _functionName + ")\n");
        if (_numLocals != 0) {
            writer.write("@" + _numLocals + "\n");
            writer.write("D=A\n");
            writer.write("@SP\n");
            writer.write("M=M+D\n");
            writer.write("A=M-1\n");
            for (int i = 0; i < _numLocals; i++) {
                writer.write("M=0\n");
                if (i != _numLocals - 1) {
                    writer.write("A=A-1\n");
                }
            }
        }
    }

    /**
     * Closes the output stream. Call when done.
     */
    public void close() throws IOException {
        writer.close();
    }
}
