package nand2tetris.compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class which handles compilation of Jack code into VM code.
 */
public class CompilationEngine {
    /**Tokenizer containing input tokens*/
    private final JackTokenizer tokenizer;
    /**Output file writer*/
    private final FileWriter writer;

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
    public void compileClass() throws IOException, SyntaxException {
        //Consume class keyword
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.KEYWORD || tokenizer.keyWord() != JackTokenizer.KeyWord.CLASS) {
            writer.close();
            throw new SyntaxException("Expected class declaration, instead found " + tokenizer);
        }
        writer.write("<class>\n");
        writeKeyWord();

        //Consume class name
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.IDENTIFIER) {
            writer.close();
            throw new SyntaxException("Expected class name, instead found " + tokenizer);
        }
        writeIdentifier();

        //Consume {
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '{') {
            writer.close();
            throw new SyntaxException("Expected { to begin class body, instead found " + tokenizer);
        }
        writeSymbol();

        boolean closed = false;
        boolean varsDone = false;
        while (!closed && tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            switch (tokenizer.tokenType()) {
                //Consume class variable and function declarations
                case KEYWORD:
                    switch (tokenizer.keyWord()) {
                        case STATIC:
                        case FIELD:
                            if (!varsDone) {
                                compileClassVarDec();
                            }
                            else {
                                writer.close();
                                throw new SyntaxException("Class variables must be declared before any functions.");
                            }
                            break;
                        case CONSTRUCTOR:
                        case FUNCTION:
                        case METHOD:
                            varsDone = true;
                            compileSubroutine();
                            break;
                        default:
                            writer.close();
                            throw new SyntaxException("Expected class variable or function declaration, instead found " + tokenizer);
                    }
                    break;
                //Consume }
                case SYMBOL:
                    if (tokenizer.symbol() == '}') {
                        closed = true;
                        writeSymbol();
                    }
                    else {
                        writer.close();
                        throw new SyntaxException("Expected class variable or function declaration, instead found " + tokenizer);
                    }
                    break;
                default:
                    writer.close();
                    throw new SyntaxException("Expected class variable or function declaration, instead found " + tokenizer);
            }
        }

        //Throw error if there are any tokens left after class body finishes
        if (tokenizer.hasMoreTokens()) {
            writer.close();
            throw new SyntaxException("Expected end of file after class body, instead found " + tokenizer);
        }
        //Throw error if class body was never closed
        if (!closed) {
            writer.close();
            throw new SyntaxException("Class body was never closed - check for mismatched brackets.");
        }

        writer.write("</class>\n");
        writer.close();
    }

    /**
     * Compiles a class variable declaration.
     */
    public void compileClassVarDec() throws IOException, SyntaxException {
        writer.write("<classVarDec>\n");

        //Consume static/field keyword (guaranteed present by caller)
        writeKeyWord();

        //Consume type
        tokenizer.advance();
        switch (tokenizer.tokenType()) {
            case KEYWORD:
                switch (tokenizer.keyWord()) {
                    case INT:
                    case CHAR:
                    case BOOLEAN:
                        writeKeyWord();
                        break;
                    default:
                        writer.close();
                        throw new SyntaxException("Expected variable type, instead found " + tokenizer);
                }
                break;
            case IDENTIFIER:
                writeIdentifier();
                break;
            default:
                writer.close();
                throw new SyntaxException("Expected variable type, instead found " + tokenizer);
        }

        //Consume varName list
        boolean expectIdentifier = true;
        boolean finished = false;
        while (!finished && tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            switch (tokenizer.tokenType()) {
                //Consume variable identifier
                case IDENTIFIER:
                    writeIdentifier();
                    expectIdentifier = false;
                    break;
                //Consume , or ;
                case SYMBOL:
                    if (expectIdentifier) {
                        writer.close();
                        throw new SyntaxException("Expected identifier, instead found ,");
                    }
                    switch (tokenizer.symbol()) {
                        case ',':
                            writeSymbol();
                            expectIdentifier = true;
                            break;
                        case ';':
                            writeSymbol();
                            finished = true;
                            break;
                        default:
                            writer.close();
                            throw new SyntaxException("Expected ',' or ';', instead found " + tokenizer.symbol());
                    }
            }
        }

        writer.write("</classVarDec>\n");
    }

    /**
     * Compiles a function, method, or constructor.
     */
    public void compileSubroutine() throws IOException, SyntaxException {
        writer.write("<subroutineDec>\n");

        //Consume constructor/function/method keyword (guaranteed present by caller)
        writeKeyWord();

        //Consume type
        tokenizer.advance();
        switch (tokenizer.tokenType()) {
            case KEYWORD:
                switch (tokenizer.keyWord()) {
                    case INT:
                    case CHAR:
                    case BOOLEAN:
                    case VOID:
                        writeKeyWord();
                        break;
                    default:
                        writer.close();
                        throw new SyntaxException("Expected variable type, instead found " + tokenizer);
                }
                break;
            case IDENTIFIER:
                writeIdentifier();
                break;
            default:
                writer.close();
                throw new SyntaxException("Expected variable type, instead found " + tokenizer);
        }

        //Consume subroutine name
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.IDENTIFIER) {
            writer.close();
            throw new SyntaxException("Expected function name, instead found " + tokenizer);
        }
        writeIdentifier();

        //Consume (
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '(') {
            writer.close();
            throw new SyntaxException("Expected '(', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume parameter list
        compileParameterList();

        //Consume )
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ')') {
            writer.close();
            throw new SyntaxException("Expected ')', instead found " + tokenizer);
        }
        writeSymbol();

        writer.write("<subroutineBody>\n");

        //Consume {
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '{') {
            writer.close();
            throw new SyntaxException("Expected '{', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume function body
        boolean closed = false;
        while (!closed && tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            if (tokenizer.tokenType() == JackTokenizer.TokenType.KEYWORD) {
                switch (tokenizer.keyWord()) {
                    //Consume variable declarations
                    case VAR:
                        compileVarDec();
                        break;
                    //Consume statements
                    case DO:
                    case LET:
                    case IF:
                    case WHILE:
                    case RETURN:
                        compileStatements();
                        closed = true;
                        break;
                    default:
                        writer.close();
                        throw new SyntaxException("Expected local variable or function declaration, instead found " + tokenizer);
                }
            }
            else {
                writer.close();
                throw new SyntaxException("Expected local variable or function declaration, instead found " + tokenizer);
            }
        }

        //Consume }
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '}') {
            writer.close();
            throw new SyntaxException("Expected '}', instead found " + tokenizer);
        }
        writeSymbol();
        writer.write("</subroutineBody>\n");
        writer.write("</subroutineDec>\n");
    }

    /**
     * Compiles a parameter list.
     */
    public void compileParameterList() throws IOException, SyntaxException {
        writer.write("<parameterList>\n");

        boolean expectType = true;
        boolean expectIdentifier = false;
        boolean expectComma = false;
        boolean finished = false;
        boolean foundParameters = false;
        while (!finished && tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            switch (tokenizer.tokenType()) {
                case KEYWORD:
                    if (expectType) {
                        switch (tokenizer.keyWord()) {
                            case INT:
                            case CHAR:
                            case BOOLEAN:
                                expectType = false;
                                expectIdentifier = true;
                                foundParameters = true;
                                writeKeyWord();
                                break;
                            default:
                                writer.close();
                                throw new SyntaxException("Expected parameter type, instead found " + tokenizer);
                        }
                    }
                    else if (expectIdentifier) {
                        writer.close();
                        throw new SyntaxException("Expected parameter name, instead found " + tokenizer);
                    }
                    else {
                        writer.close();
                        throw new SyntaxException("Parameter list must be separated by commas.");
                    }
                    break;
                case IDENTIFIER:
                    if (expectType) {
                        expectType = false;
                        expectIdentifier = true;
                        foundParameters = true;
                        writeIdentifier();
                    }
                    else if (expectIdentifier) {
                        expectIdentifier = false;
                        expectComma = true;
                        writeIdentifier();
                    }
                    else {
                        writer.close();
                        throw new SyntaxException("Parameter list must be separated by commas.");
                    }
                    break;
                case SYMBOL:
                    switch (tokenizer.symbol()) {
                        case ',':
                            if (expectComma) {
                                expectComma = false;
                                expectType = true;
                                writeSymbol();
                            }
                            break;
                        case ')':
                            if (expectComma || !foundParameters) {
                                finished = true;
                            }
                            else {
                                writer.close();
                                throw new SyntaxException("Parameter list terminated early.");
                            }
                            break;
                        default:
                            writer.close();
                            throw new SyntaxException("Illegal token " + tokenizer + " encountered in parameter list.");
                    }
                    break;
                default:
                    writer.close();
                    throw new SyntaxException("Illegal token " + tokenizer + " encountered in parameter list.");
            }
        }

        writer.write("</parameterList>\n");
    }

    /**
     * Compiles a local variable declaration.
     */
    public void compileVarDec() throws IOException, SyntaxException {
        writer.write("<varDec>\n");

        //Consume var keyword (guaranteed present by caller)
        writeKeyWord();

        //Consume type
        tokenizer.advance();
        switch (tokenizer.tokenType()) {
            case KEYWORD:
                switch (tokenizer.keyWord()) {
                    case INT:
                    case CHAR:
                    case BOOLEAN:
                        writeKeyWord();
                        break;
                    default:
                        writer.close();
                        throw new SyntaxException("Expected variable type, instead found " + tokenizer);
                }
                break;
            case IDENTIFIER:
                writeIdentifier();
                break;
            default:
                writer.close();
                throw new SyntaxException("Expected variable type, instead found " + tokenizer);
        }

        //Consume varName list
        boolean expectIdentifier = true;
        boolean finished = false;
        while (!finished && tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            switch (tokenizer.tokenType()) {
                //Consume variable identifier
                case IDENTIFIER:
                    writeIdentifier();
                    expectIdentifier = false;
                    break;
                //Consume , or ;
                case SYMBOL:
                    if (expectIdentifier) {
                        writer.close();
                        throw new SyntaxException("Expected identifier, instead found ,");
                    }
                    switch (tokenizer.symbol()) {
                        case ',':
                            writeSymbol();
                            expectIdentifier = true;
                            break;
                        case ';':
                            writeSymbol();
                            finished = true;
                            break;
                        default:
                            writer.close();
                            throw new SyntaxException("Expected ',' or ';', instead found " + tokenizer.symbol());
                    }
            }
        }

        writer.write("</varDec>\n");
    }

    /**
     * Compiles a sequence of statements.
     */
    public void compileStatements() throws IOException, SyntaxException {
        writer.write("<statements>\n");

        //Consume statements until a non-statement token is found, then return
        boolean statement = true;
        while (statement && tokenizer.hasMoreTokens()) {
            if (tokenizer.tokenType() == JackTokenizer.TokenType.KEYWORD) {
                switch (tokenizer.keyWord()) {
                    case LET:
                        compileLet();
                        break;
                    case IF:
                        compileIf();
                        break;
                    case WHILE:
                        compileWhile();
                        break;
                    case DO:
                        compileDo();
                        break;
                    case RETURN:
                        compileReturn();
                        break;
                    default:
                        statement = false;
                }
            }
            else {
                statement = false;
            }
        }

        writer.write("</statements>\n");
    }

    /**
     * Compiles a command statement.
     */
    public void compileDo() throws IOException, SyntaxException {
        writer.write("<doStatement>\n");

        //Consume do keyword (presence guaranteed by caller)
        writeKeyWord();

        //Consume subroutine name
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.IDENTIFIER) {
            writer.close();
            throw new SyntaxException("Expected function, class, or variable name, instead found " + tokenizer);
        }
        writeIdentifier();

        //Consume ( or .
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || (tokenizer.symbol() != '(' && tokenizer.symbol() != '.')) {
            writer.close();
            throw new SyntaxException("Expected '(' or '.', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume identifier and ( if this is a member call
        if (tokenizer.symbol() == '.') {
            //Consume identifier
            tokenizer.advance();
            if (tokenizer.tokenType() != JackTokenizer.TokenType.IDENTIFIER) {
                writer.close();
                throw new SyntaxException("Expected function name, instead found " + tokenizer);
            }
            writeIdentifier();

            //Consume (
            tokenizer.advance();
            if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '(') {
                writer.close();
                throw new SyntaxException("Expected '(', instead found " + tokenizer);
            }
            writeSymbol();
        }

        //Consume expression list
        tokenizer.advance();
        compileExpressionList();

        //Consume )
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ')') {
            writer.close();
            throw new SyntaxException("Expected ')', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume ;
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ';') {
            writer.close();
            throw new SyntaxException("Expected ';', instead found " + tokenizer);
        }
        writeSymbol();

        //Position tokenizer at next statement head
        tokenizer.advance();

        writer.write("</doStatement>\n");
    }

    /**
     * Compiles an assign statement.
     */
    public void compileLet() throws IOException, SyntaxException {
        writer.write("<letStatement>\n");

        //Consume let keyword (presence guaranteed by caller)
        writeKeyWord();

        //Consume variable name
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.IDENTIFIER) {
            writer.close();
            throw new SyntaxException("Expected variable name, instead found " + tokenizer);
        }
        writeIdentifier();

        //Consume [] term, if any
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || (tokenizer.symbol() != '[' && tokenizer.symbol() != '=')) {
            writer.close();
            throw new SyntaxException("Expected '[' or '=', instead found " + tokenizer);
        }
        if (tokenizer.symbol() == '[') {
            //Consume [
            writeSymbol();

            //Consume expression
            tokenizer.advance();
            compileExpression();

            //Consume ]
            if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ']') {
                writer.close();
                throw new SyntaxException("Expected ']', instead found " + tokenizer);
            }
            writeSymbol();

            //Guarantee presence of =
            tokenizer.advance();
            if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '=') {
                writer.close();
                throw new SyntaxException("Expected '=', instead found " + tokenizer);
            }
        }

        //Consume =
        writeSymbol();

        //Consume expression
        tokenizer.advance();
        compileExpression();

        //Consume ;
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ';') {
            writer.close();
            throw new SyntaxException("Expected ';', instead found " + tokenizer);
        }
        writeSymbol();

        //Position tokenizer at next statement head
        tokenizer.advance();

        writer.write("</letStatement>\n");
    }

    /**
     * Compiles a while loop.
     */
    public void compileWhile() throws IOException, SyntaxException {
        writer.write("<whileStatement>\n");

        //Consume while keyword (presence guaranteed by caller)
        writeKeyWord();

        //Consume (
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '(') {
            writer.close();
            throw new SyntaxException("Expected '(', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume expression
        tokenizer.advance();
        compileExpression();

        //Consume )
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ')') {
            writer.close();
            throw new SyntaxException("Expected ')', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume {
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '{') {
            writer.close();
            throw new SyntaxException("Expected '{', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume statements
        tokenizer.advance();
        compileStatements();

        //Consume }
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '}') {
            writer.close();
            throw new SyntaxException("Expected '}', instead found " + tokenizer);
        }
        writeSymbol();

        //Advance tokenizer to next token for caller
        tokenizer.advance();

        writer.write("</whileStatement>\n");
    }

    /**
     * Compiles a return statement.
     */
    public void compileReturn() throws IOException, SyntaxException {
        writer.write("<returnStatement>\n");

        //Consume return keyword (presence guaranteed by caller)
        writeKeyWord();

        //Consume expression, if any
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ';') {
            compileExpression();
        }

        //Consume ;
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ';') {
            writer.close();
            throw new SyntaxException("Expected ';', instead found " + tokenizer);
        }
        writeSymbol();

        //Position tokenizer at next statement head
        tokenizer.advance();

        writer.write("</returnStatement>\n");
    }

    /**
     * Compiles an if/else statement.
     */
    public void compileIf() throws IOException, SyntaxException {
        writer.write("<ifStatement>\n");

        //Consume if keyword (presence guaranteed by caller)
        writeKeyWord();

        //Consume (
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '(') {
            writer.close();
            throw new SyntaxException("Expected '(', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume expression
        tokenizer.advance();
        compileExpression();

        //Consume )
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ')') {
            writer.close();
            throw new SyntaxException("Expected ')', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume {
        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '{') {
            writer.close();
            throw new SyntaxException("Expected '{', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume statements
        tokenizer.advance();
        compileStatements();

        //Consume }
        if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '}') {
            writer.close();
            throw new SyntaxException("Expected '}', instead found " + tokenizer);
        }
        writeSymbol();

        //Consume else block, if present
        tokenizer.advance();
        if (tokenizer.tokenType() == JackTokenizer.TokenType.KEYWORD && tokenizer.keyWord() == JackTokenizer.KeyWord.ELSE) {
            //Consume else keyword
            writeKeyWord();

            //Consume {
            tokenizer.advance();
            if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '{') {
                writer.close();
                throw new SyntaxException("Expected '{', instead found " + tokenizer);
            }
            writeSymbol();

            //Consume statements
            tokenizer.advance();
            compileStatements();

            //Consume }
            if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '}') {
                writer.close();
                throw new SyntaxException("Expected '}', instead found " + tokenizer);
            }
            writeSymbol();

            //Advance tokenizer for caller
            tokenizer.advance();
        }

        writer.write("</ifStatement>\n");
    }

    /**
     * Compiles an arithmetic expression.
     */
    public void compileExpression() throws IOException, SyntaxException {
        writer.write("<expression>\n");

        //Consume term
        compileTerm();

        //Consume (op term)*
        while (tokenizer.hasMoreTokens()) {
            //Check for operator - if none found, we're done
            if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || "+-*/&|<>=".indexOf(tokenizer.symbol()) == -1) {
                break;
            }
            writeSymbol();

            //Consume next term
            tokenizer.advance();
            compileTerm();
        }

        writer.write("</expression>\n");
    }

    /**
     * Compiles a single expression term.
     */
    public void compileTerm() throws IOException, SyntaxException {
        writer.write("<term>\n");

        switch (tokenizer.tokenType()) {
            //Consume keyword constant
            case KEYWORD:
                switch (tokenizer.keyWord()) {
                    case TRUE:
                    case FALSE:
                    case NULL:
                    case THIS:
                        writeKeyWord();
                        //Advance tokenizer to next token
                        tokenizer.advance();
                        break;
                    default:
                        writer.close();
                        throw new SyntaxException("Expected expression term, instead found " + tokenizer);
                }
                break;

            case SYMBOL:
                //Consume unary operator
                if (tokenizer.symbol() == '-' || tokenizer.symbol() == '~') {
                    writeSymbol();

                    //Consume operand term
                    tokenizer.advance();
                    compileTerm();
                }
                //Consume parenthesized expression
                else if (tokenizer.symbol() == '(') {
                    writeSymbol();

                    //Consume expression
                    tokenizer.advance();
                    compileExpression();

                    //Consume )
                    if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ')') {
                        writer.close();
                        throw new SyntaxException("Expected ')', instead found " + tokenizer);
                    }
                    writeSymbol();

                    //Advance tokenizer to next token
                    tokenizer.advance();
                }
                else {
                    writer.close();
                    throw new SyntaxException("Expected expression term, instead found " + tokenizer);
                }
                break;
            case IDENTIFIER:
                //Consume identifier
                writeIdentifier();

                tokenizer.advance();
                //Consume [expression]
                if (tokenizer.tokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.symbol() == '[') {
                    //Consume [
                    writeSymbol();

                    //Consume expression
                    tokenizer.advance();
                    compileExpression();

                    //Consume ]
                    if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ']') {
                        writer.close();
                        throw new SyntaxException("Expected ']', instead found " + tokenizer);
                    }
                    writeSymbol();

                    //Advance tokenizer to next token
                    tokenizer.advance();
                }
                //Consume (expressionList)
                else if (tokenizer.tokenType() == JackTokenizer.TokenType.SYMBOL && (tokenizer.symbol() == '(')) {
                    //Consume (
                    writeSymbol();

                    //Consume expression list
                    tokenizer.advance();
                    compileExpressionList();

                    //Consume )
                    if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ')') {
                        writer.close();
                        throw new SyntaxException("Expected ')', instead found " + tokenizer);
                    }
                    writeSymbol();

                    //Advance tokenizer to next token
                    tokenizer.advance();
                }
                //Consume .subroutineName(expressionList)
                else if (tokenizer.tokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.symbol() == '.') {
                    //Consume .
                    writeSymbol();

                    //Consume subroutine name
                    tokenizer.advance();
                    if (tokenizer.tokenType() != JackTokenizer.TokenType.IDENTIFIER) {
                        writer.close();
                        throw new SyntaxException("Expected subroutine name, instead found " + tokenizer);
                    }
                    writeIdentifier();

                    //Consume (
                    tokenizer.advance();
                    if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != '(') {
                        writer.close();
                        throw new SyntaxException("Expected '(', instead found " + tokenizer);
                    }
                    writeSymbol();

                    //Consume expression list
                    tokenizer.advance();
                    compileExpressionList();

                    //Consume )
                    if (tokenizer.tokenType() != JackTokenizer.TokenType.SYMBOL || tokenizer.symbol() != ')') {
                        writer.close();
                        throw new SyntaxException("Expected ')', instead found " + tokenizer);
                    }
                    writeSymbol();

                    //Advance tokenizer to next token
                    tokenizer.advance();
                }
                //For simple variable usage, no action needed
                break;
            //Consume integer literal
            case INT_CONST:
                writeIntConstant();
                //Advance tokenizer to next token
                tokenizer.advance();
                break;
            //Consume string literal
            case STRING_CONST:
                writeStringConstant();
                //Advance tokenizer to next token
                tokenizer.advance();
                break;
        }

        writer.write("</term>\n");
    }

    /**
     * Compiles a list of expressions.
     */
    public void compileExpressionList() throws IOException, SyntaxException {
        writer.write("<expressionList>\n");

        //Handle empty expression list - expression lists are always terminated by )
        if (tokenizer.tokenType() == JackTokenizer.TokenType.SYMBOL && tokenizer.symbol() == ')') {
            writer.write("</expressionList>\n");
            return;
        }

        boolean expectExpression = true;
        boolean finished = false;
        while (!finished && tokenizer.hasMoreTokens()) {
            if (expectExpression) {
                compileExpression();
                expectExpression = false;
            }
            else {
                if (tokenizer.tokenType() == JackTokenizer.TokenType.SYMBOL) {
                    switch (tokenizer.symbol()) {
                        case ',':
                            writeSymbol();
                            expectExpression = true;
                            tokenizer.advance();
                            break;
                        case ')':
                            finished = true;
                            break;
                        default:
                            writer.close();
                            throw new SyntaxException("Expected ',' or ')', instead found " + tokenizer);
                    }
                }
                else {
                    writer.close();
                    throw new SyntaxException("Expected ',' or ')', instead found " + tokenizer);
                }
            }
        }

        writer.write("</expressionList>\n");
    }

    /**
     * Writes a keyword terminal.
     */
    private void writeKeyWord() throws IOException {
        writer.write("<keyword> " + tokenizer.keyWord().toString().toLowerCase() + " </keyword>\n");
    }

    /**
     * Writes a symbol terminal.
     */
    private void writeSymbol() throws IOException {
        if (tokenizer.symbol() == '<') {
            writer.write("<symbol> &lt; </symbol>\n");
        }
        else if (tokenizer.symbol() == '>') {
            writer.write("<symbol> &gt; </symbol>\n");
        }
        else if (tokenizer.symbol() == '&') {
            writer.write("<symbol> &amp; </symbol>\n");
        }
        else {
            writer.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
        }
    }

    /**
     * Writes an integer terminal.
     */
    private void writeIntConstant() throws IOException {
        writer.write("<integerConstant> " + tokenizer.intVal() + " </integerConstant>\n");
    }

    /**
     * Writes a string terminal.
     */
    private void writeStringConstant() throws IOException {
        writer.write("<stringConstant> " + tokenizer.stringVal() + " </stringConstant>\n");
    }

    /**
     * Writes an identifier terminal.
     */
    private void writeIdentifier() throws IOException {
        writer.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
    }
}
