package nand2tetris.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class which handles compilation of Jack code into VM code.
 */
public class CompilationEngine {
    /**Tokenizer containing input tokens*/
    private JackTokenizer tokenizer;
    /**Output file writer*/
    private FileWriter writer;

    /**
     * Constructs a new compiler engine with the given input and output.
     *
     * @param _input A JackTokenizer object containing tokenized input.
     * @param _output An output file pointing to the output .vm file.
     */
    public CompilationEngine(JackTokenizer _input, File _output) throws IOException {
        tokenizer = _input;
        writer = new FileWriter(_output);
    }

    /**
     * Compiles a class.
     */
    public void compileClass() throws IOException {
        writer.write("<class>\n");
        //TODO: Implement
        writer.write("</class>\n");
        writer.close();
    }

    /**
     * Compiles a class variable declaration.
     */
    public void compileClassVarDec() throws IOException {
        writer.write("<classVarDec>\n");
        //TODO: Implement
        writer.write("</classVarDec>\n");
    }

    /**
     * Compiles a function, method, or constructor.
     */
    public void compileSubroutine() throws IOException {
        writer.write("<subroutineDec>\n");
        //TODO: Implement
        writer.write("</subroutineDec>\n");
    }

    /**
     * Compiles a parameter list.
     */
    public void compileParameterList() throws IOException {
        writer.write("<parameterList>\n");
        //TODO: Implement
        writer.write("</parameterList>\n");
    }

    /**
     * Compiles a local variable declaration.
     */
    public void compileVarDec() throws IOException {
        writer.write("<varDec>\n");
        //TODO: Implement
        writer.write("</varDec>\n");
    }

    /**
     * Compiles a sequence of statements.
     */
    public void compileStatements() throws IOException {
        writer.write("<statements>\n");
        //TODO: Implement
        writer.write("</statements>\n");
    }

    /**
     * Compiles a command statement.
     */
    public void compileDo() throws IOException {
        writer.write("<doStatement>\n");
        //TODO: Implement
        writer.write("</doStatement>\n");
    }

    /**
     * Compiles an assign statement.
     */
    public void compileLet() throws IOException {
        writer.write("<letStatement>\n");
        //TODO: Implement
        writer.write("</letStatement\n");
    }

    /**
     * Compiles a while loop.
     */
    public void compileWhile() throws IOException {
        writer.write("<whileStatement>\n");
        //TODO: Implement
        writer.write("</whileStatement\n");
    }

    /**
     * Compiles a return statement.
     */
    public void compileReturn() throws IOException {
        writer.write("<returnStatement>\n");
        //TODO: Implement
        writer.write("</returnStatement\n");
    }

    /**
     * Compiles an if/else statement.
     */
    public void compileIf() throws IOException {
        writer.write("<ifStatement>\n");
        //TODO: Implement
        writer.write("</ifStatement\n");
    }

    /**
     * Compiles an arithmetic expression.
     */
    public void compileExpression() throws IOException {
        writer.write("<expression>\n");
        //TODO: Implement
        writer.write("</expression\n");
    }

    /**
     * Compiles a single expression term.
     */
    public void compileTerm() throws IOException {
        writer.write("<term>\n");
        //TODO: Implement
        writer.write("</term\n");
    }

    /**
     * Compiles a list of expressions.
     */
    public void compileExpressionList() throws IOException {
        writer.write("<expressionList>\n");
        //TODO: Implement
        writer.write("</expressionList\n");
    }
}
