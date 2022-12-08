/* Complete all the methods.
EBNF of Mini Language
Program" --> "("Sequence State")".
Sequence --> "("Statements")".
Statements --> Stmt*
Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
State -->  "("Pairs")".
Pairs -->  Pair*.
Pair --> "("Identifier Literal")".
NullStatement --> "skip".
Assignment --> "assign" Identifier Expression.
Conditional --> "conditional" Expression Stmt Stmt.
Loop --> "loop" Expression Stmt.
Block --> "block" Statements.
Expression --> Identifier | Literal | "("Operation Expression Expression")".
Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".

Note: Treat Identifier and Literal as terminal symbols. Every symbol inside " and " is a terminal symbol. The rest are non terminals.

*/
public class Parser {
    private Token currentToken;
    Scanner scanner;

    private void accept(byte expectedKind) {
        if (currentToken.kind == expectedKind)
            currentToken = scanner.scan();
        else
            new Error("Syntax error: " + currentToken.spelling + " is not expected.",
                    currentToken.line);
    }

    private void acceptIt() {
        currentToken = scanner.scan();
    }

    public void parse() {
        SourceFile sourceFile = new SourceFile();
        scanner = new Scanner(sourceFile.openFile());
        currentToken = scanner.scan();
        parseProgram();
    }

    //Program" --> "("Sequence State")".
    private void parseProgram() {
        if (currentToken.kind != Token.NOTHING && currentToken.kind != Token.EOT) {
            if (currentToken.kind == 9) {
                accept((byte) 9);
            }
            parseSequence();
            parseState();
            if (currentToken.kind == 10) {
                accept((byte) 10);
            }
        }
    }

    //Sequence --> "("Statements")".
    private void parseSequence() {
        if (currentToken.kind == 9) {
            accept((byte) 9);
        }
        parseStatements();
        if (currentToken.kind == 10) {
            accept((byte) 10);
        }
    }

    //Statements --> Stmt*
    private void parseStatements() {
        byte tokenValue = currentToken.kind;
        while ((tokenValue >= 2 && tokenValue <= 6) || tokenValue == 9 || tokenValue == 10) {
            parseStmt();
            tokenValue = currentToken.kind;
        }
    }

    //Stmt --> "(" {NullStatement | Assignment | Conditional | Loop | Block}")".
    private void parseStmt() {
        if (currentToken.kind == 9) {
            accept((byte) 9);
        }
        if (currentToken.kind == 2) {
            parseAssignment();
        } else if (currentToken.kind == 3) {
            parseConditional();
        } else if (currentToken.kind == 4) {
            parseLoop();
        } else if (currentToken.kind == 5) {
            parseBlock();
        } else {
            parseNullStatement();
        }
        if (currentToken.kind == 10) {
            accept((byte) 10);
        }
    }

    //State -->  "("Pairs")".
    private void parseState() {
        if (currentToken.kind == 9) {
            accept((byte) 9);
        }
        parsePairs();
        if (currentToken.kind == 10) {
            accept((byte) 10);
        }
    }

    //Pairs --> Pair*.
    private void parsePairs() {
        byte tokenValue = currentToken.kind;
        while (tokenValue == 9 || tokenValue == 0 || tokenValue == 1 || tokenValue == 10) {
            parsePair();
            tokenValue = currentToken.kind;
        }
    }

    //Pair --> "("Identifier Literal")".
    private void parsePair() {
        if (currentToken.kind == 9) {
            accept((byte) 9);
        }
        if (currentToken.kind == 0) {
            acceptIt();
        } else if (currentToken.kind == 1) {
            acceptIt();
        }
        if (currentToken.kind == 10) {
            accept((byte) 10);
        }
    }

    //NullStatement --> skip.
    private void parseNullStatement() {
        acceptIt();
    }

    //Assignment --> "assign" Identifier Expression.
    private void parseAssignment() {
        acceptIt();
        if (currentToken.kind == 0) {
            acceptIt();
        }
        parseExpression();
    }

    //Conditional --> "conditional" Expression Stmt Stmt.
    private void parseConditional() {
        acceptIt();
        parseExpression();
        parseStmt();
        parseStmt();
    }

    //Loop --> "loop" Expression Stmt.
    private void parseLoop() {
        acceptIt();
        parseExpression();
        parseStmt();
    }

    //Block --> "block" Statements.
    private void parseBlock() {
        acceptIt();
        parseStatements();
    }

    //Expression --> Identifier | Literal | "("Operation Expression Expression")".
    private void parseExpression() {
        if (currentToken.kind == 0 || currentToken.kind == 1) {
            acceptIt();
        } else if (currentToken.kind == 9) {
            accept((byte) 9);
        }
        parseOperation();
        parseExpression();
        parseExpression();
        if (currentToken.kind == 10) {
            accept((byte) 10);
        }

    }


    //Operation --> "+" |"-" | "*" | "/" | "<" | "<=" | ">" | ">=" | "=" | "!=" | "or" | "and".
    private void parseOperation() {
        if (currentToken.kind == 11 || currentToken.kind == 7 || currentToken.kind == 8) {
            acceptIt();
        }
    }

}