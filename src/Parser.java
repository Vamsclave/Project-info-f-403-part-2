import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Parser for PascalMaisPresque.
 * 
 * The parser implements a recursive descend mimicking the run of the pushdown automaton: the call stack replacing the automaton stack.
 * 
 */
public class Parser{
    /**
     * Lexer object for the parsed file.
     */
    private LexicalAnalyzer scanner;
    /**
     * Current symbol at the head of the word to be read. This corresponds to the look-ahead (of length 1).
     */
    private Symbol current;
    /**
     * Option to print only the rule number (false) or the full rule (true).
     */
    private boolean fullRuleDisplay=false;
    /**
     * Width (in characters) of the widest left handside in a production rule.
     */
    private static final int widestNonTerm=14; // <InstListTail>
    /**
     * Width (in characters) of the highest rule number.
     */
    private static final int log10ruleCard=2; // 41 rules

    /**
     * Creates a Parser object for the provided file and initialized the look-ahead.
     * 
     * @param source a FileReader object for the parsed file.
     * @throws IOException in case the lexing fails (syntax error).
     */
    public Parser(FileReader source) throws IOException{
        this.scanner = new LexicalAnalyzer(source);
        this.current = scanner.nextToken();
    }
    
    /* Display of the rules */
    /**
     * Returns a string of several spaces.
     * 
     * @param n the number of spaces.
     * @return a String containing n spaces.
     */
    private static String multispace(int n) {
        String res="";
        for (int i=0;i<n;i++) {
            res+=" ";
        };
        return res;
    }
    
    /**
     * Outputs the rule used in the LL descent.
     * 
     * @param rNum the rule number.
     * @param ruleLhs the left hand-side of the rule as a String.
     * @param ruleRhs the right hand-side of the rule as a String.
     * @param full a boolean specifying whether to write only the rule number (false) or the full rule (true).
     */
    private static void ruleOutput(int rNum, String ruleLhs,String ruleRhs, boolean full) {
        if (full) {
            System.out.println("   ["+rNum+"]"+
                multispace(1+log10ruleCard-String.valueOf(rNum).length())+ // Align left hand-sides regardless of number of digits in rule number
                ruleLhs+multispace(2+widestNonTerm-ruleLhs.length())+ // Align right hand-sides regardless of length of the left hand-side
                "→  "+ruleRhs);
        } else {
            System.out.print(rNum+" ");
        }
    }
    
    /**
     * Outputs the rule used in the LL descent, using the fullRuleDisplay value to set the option of full display or not.
     * 
     * @param rNum the rule number.
     * @param ruleLhs the left hand-side of the rule as a String.
     * @param ruleRhs the right hand-side of the rule as a String.
     */
    private void ruleOutput(int rNum, String ruleLhs,String ruleRhs) {
        ruleOutput(rNum,ruleLhs,ruleRhs,this.fullRuleDisplay);
    }
    
    /**
     * Sets the display option to "Full rules".
     */
    public void displayFullRules() {
        this.fullRuleDisplay=true;
    }
    
    /**
     * Sets the display option to "Rule numbers only".
     */
    public void displayRuleNumbers() {
        this.fullRuleDisplay=false;
    }

    /* Matching of terminals */
    /**
     * Advances in the input stream, consuming one token.
     * 
     * @throws IOException in case the lexing fails (syntax error).
     */
    private void consume() throws IOException{
        this.current = scanner.nextToken();
    }

    /**
     * Matches a (terminal) token from the head of the word.
     * 
     * @param token then LexicalUnit (terminal) to be matched.
     * @throws IOException in case the lexing fails (syntax error).
     * @throws ParseException in case the matching fails (syntax error): the next tolen is not the one to be matched.
     * @return a ParseTree made of a single leaf (the matched terminal).
     */
    private ParseTree match(LexicalUnit token) throws IOException, ParseException{
        if(!current.getType().equals(token)){
            // There is a parsing error
            throw new ParseException(current, Arrays.asList(token));
        }
        else {
            Symbol cur = current;
            consume();
            return new ParseTree(cur);
        }
    }
    
    /* Applying grammar rules */
    /**
     * Parses the file.
     * 
     * @return a ParseTree containing the parsed file structured by the grammar rules.
     * @throws IOException in case the lexing fails (syntax error).
     * @throws ParseException in case the parsing fails (syntax error).
     */
    public ParseTree parse() throws IOException, ParseException{
        // Program is the initial symbol of the grammar
        ParseTree pt = program();
        if (!this.fullRuleDisplay) {System.out.println();} // New line at the end of list of rules
        return pt;
    }
    
    /**
     * Parses <Program> -> LET <ProgName> BE <Code> END
     */
    private ParseTree program() throws IOException, ParseException{
        // [1] <Program>  ->  begin <Code> end
        ruleOutput(1,"<Program>","LET [ProgName] BE <Code> END");
        return new ParseTree(NonTerminal.Program, Arrays.asList(
            match(LexicalUnit.LET),
            match(LexicalUnit.PROGNAME),
            match(LexicalUnit.BE),
            code(),
            match(LexicalUnit.END)
        ));
    }
    
    /**
     * Parses <Code> -> <Instruction> : <Code> | ε
     */
    private ParseTree code() throws IOException, ParseException{
        switch(current.getType()) {
            // [2] <Code>  ->  <Instruction>
            case VARNAME:
            case IF:
            case WHILE:
            case OUTPUT:
            case INPUT:
                ruleOutput(2,"<Code>","<Instruction> : <Code>");
                return new ParseTree(NonTerminal.Code, Arrays.asList(
                    instruction()
                ));
            // [3] <Code>  ->  EPSILON 
            case END:
                ruleOutput(3,"<Code>","ɛ");
                return new ParseTree(NonTerminal.Code, Arrays.asList(
                    new ParseTree(LexicalUnit.EPSILON)
                ));
            default:
                throw new ParseException(current,NonTerminal.Code,Arrays.asList(
                    LexicalUnit.IF,
                    LexicalUnit.WHILE,
                    LexicalUnit.OUTPUT,
                    LexicalUnit.INPUT,
                    LexicalUnit.VARNAME
                ));
        }
    }

    /**
     * Treats a <Instruction> at the top of the stack.
     */
    private ParseTree instruction() throws IOException, ParseException{
        switch(current.getType()) {
            // [4] <Instruction>  ->  <Assign>
            case VARNAME:
                ruleOutput(4, "<Instruction>", "<Assign>");
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(
                    assignExpr()
                ));
            // [5] <Instruction>  ->  <If>
            case IF:
                ruleOutput(5, "<Instruction>", "<If>");
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(
                    ifExpr()
                ));
            // [6] <Instruction>  ->  <While>
            case WHILE:
                ruleOutput(6, "<Instruction>", "<While>");
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(
                    whileExpr()
                ));
            // [7] <Instruction>  ->  <Output>
            case OUTPUT:
                ruleOutput(7, "<Instruction>", "<Output>");
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(
                    outputExpr()
                ));
            // [8] <Instruction>  ->  <Input>
            case INPUT:
                ruleOutput(8, "<Instruction>", "<Input>");
                return new ParseTree(NonTerminal.Instruction, Arrays.asList(
                    inputExpr()
                ));
            default:
                throw new ParseException(current,NonTerminal.Instruction,Arrays.asList(
                    LexicalUnit.VARNAME,
                    LexicalUnit.IF,
                    LexicalUnit.WHILE,
                    LexicalUnit.OUTPUT,
                    LexicalUnit.INPUT
                ));
        }
    }
    
    /**
     * Parses <Assign> -> [VarName] = <ExprArith>
     */
    private ParseTree assignExpr() throws IOException, ParseException{
        // [9] <Assign>  ->  [VarName] = <ExprArith>
        ruleOutput(9,"<Assign>","[VarName] = <ExprArith>");
        return new ParseTree(NonTerminal.Assign, Arrays.asList(
            match(LexicalUnit.VARNAME),
            match(LexicalUnit.ASSIGN),
            exprArith()
        ));
    }
    
    /**
     * Parses <ExprArith> based on the grammar rules provided,
     * handling variables, numbers, parentheses, and unary minus.
     */
    private ParseTree exprArith() throws IOException, ParseException{
        switch (current.getType()) {
            // [10] <ExprArith>  ->  [VarName]
        case VARNAME:
            ruleOutput(10, "<ExprArith>", "[VarName]");
            return new ParseTree(NonTerminal.ExprArith, Arrays.asList(
                match(LexicalUnit.VARNAME)
            ));
        // [11] <ExprArith>  ->  [Number]
        case NUMBER:
            ruleOutput(11, "<ExprArith>", "[Number]");
            return new ParseTree(NonTerminal.ExprArith, Arrays.asList(
                match(LexicalUnit.NUMBER)
            ));
        // [12] <ExprArith>  ->  (<ExprArith>)
        case LPAREN:
            ruleOutput(12, "<ExprArith>", "(<ExprArith>)");
            return new ParseTree(NonTerminal.ExprArith, Arrays.asList(
                match(LexicalUnit.LPAREN),
                exprArith(),
                match(LexicalUnit.RPAREN)
            ));
        // [13] <ExprArith>  ->  - <ExprArith>
        case MINUS:
            ruleOutput(13, "<ExprArith>", "- <ExprArith>");
            return new ParseTree(NonTerminal.ExprArith, Arrays.asList(
                match(LexicalUnit.MINUS),
                exprArith()
            ));
        // [14] <ExprArith>  ->  <ExprArith> <Op> <ExprArith>
        case IMPLIES:
            ruleOutput(13, "<ExprArith>", "<ExprArith> <Op> <ExprArith>");
            return new ParseTree(NonTerminal.ExprArith, Arrays.asList(
                exprArith(),
                op(),
                exprArith()
            ));
        default:
            throw new ParseException(current, NonTerminal.ExprArith, Arrays.asList(
                LexicalUnit.VARNAME, 
                LexicalUnit.NUMBER, 
                LexicalUnit.LPAREN, 
                LexicalUnit.MINUS,
                LexicalUnit.IMPLIES
            ));
        }
    }

    /**
     * Parses <Op> -> + | - | * | /
     */
    private ParseTree op() throws IOException, ParseException{
        switch (current.getType()) {
        // [14] <Op> -> +
        case PLUS:
            ruleOutput(14, "<Op>", "+");
            return new ParseTree(NonTerminal.Op, Arrays.asList(
                match(LexicalUnit.PLUS)
            ));
        // [15] <Op> -> -
        case MINUS:
            ruleOutput(15, "<Op>", "-");
            return new ParseTree(NonTerminal.Op, Arrays.asList(
                match(LexicalUnit.MINUS)
            ));
        // [16] <Op> -> *
        case TIMES:
            ruleOutput(16, "<Op>", "*");
            return new ParseTree(NonTerminal.Op, Arrays.asList(
                match(LexicalUnit.TIMES)
            ));
        // [17] <Op> -> /
        case DIVIDE:
            ruleOutput(17, "<Op>", "/");
            return new ParseTree(NonTerminal.Op, Arrays.asList(
                match(LexicalUnit.DIVIDE)
            ));
        default:
            throw new ParseException(current, NonTerminal.Op, Arrays.asList(
                LexicalUnit.PLUS,
                LexicalUnit.MINUS,
                LexicalUnit.TIMES,
                LexicalUnit.DIVIDE
            ));
        }
    }
    
    /**
     * Parses <If> -> IF { <Cond> } THEN <Code> END | IF { <Cond> } THEN <Code> ELSE <Code> END
     */
    private ParseTree ifExpr() throws IOException, ParseException{
        // [18] <If> -> IF { <Cond> } THEN <Code> END | IF { <Cond> } THEN <Code> ELSE <Code> END
        ruleOutput(18, "<If>", "IF { <Cond> } THEN <Code> ELSE <Code> END");
        return new ParseTree(NonTerminal.If, Arrays.asList(
            match(LexicalUnit.IF),
            match(LexicalUnit.LBRACK),
            cond(),
            match(LexicalUnit.RBRACK),
            match(LexicalUnit.THEN),
            code(),
            match(LexicalUnit.ELSE),
            code(),
            match(LexicalUnit.END)
        ));
    }
    
    /**
     * Parses <Cond> -> <ExprArith> <Comp> <ExprArith>
     */
    private ParseTree cond() throws IOException, ParseException{
        // [19] <Cond> -> <ExprArith> <Comp> <ExprArith>
        ruleOutput(19, "<Cond>", "<ExprArith> <Comp> <ExprArith>");
        return new ParseTree(NonTerminal.Cond, Arrays.asList(
            exprArith(),
            comp(),
            exprArith()
        ));
    }

    /**
     * Parses <Comp> ->  ==  | <= || <
     */
    private ParseTree comp() throws IOException, ParseException {
        switch (current.getType()) {
            // [25] <Comp> -> ==
            case EQUAL:
                ruleOutput(25, "<Comp>", "==");
                return new ParseTree(NonTerminal.Comp, Arrays.asList(
                    match(LexicalUnit.EQUAL)
                ));
            // [22] <Comp> -> <=
            case SMALEQ:
                ruleOutput(22, "<Comp>", "<=");
                return new ParseTree(NonTerminal.Comp, Arrays.asList(
                    match(LexicalUnit.SMALEQ)
                ));
            // [21] <Comp> -> <
            case SMALLER:
                ruleOutput(21, "<Comp>", "<");
                return new ParseTree(NonTerminal.Comp, Arrays.asList(
                    match(LexicalUnit.SMALLER)
                ));
            default:
                throw new ParseException(current, NonTerminal.Comp, Arrays.asList(
                    LexicalUnit.EQUAL,
                    LexicalUnit.SMALEQ,
                    LexicalUnit.SMALLER
                ));
        }
    }
    
    /**
     * Parses <WHILE> -> WHILE { <Cond> } REPEAT <Code> END
     */
    private ParseTree whileExpr() throws IOException, ParseException{
        // [22] <WHILE>  ->  WHILE { <Cond> } REPEAT <Code> END
        ruleOutput(22,"<WHILE>","WHILE { <Cond> } REPEAT <Code> END");
        return new ParseTree(NonTerminal.While, Arrays.asList(
            match(LexicalUnit.WHILE),
            match(LexicalUnit.LBRACK),
            cond(),
            match(LexicalUnit.RBRACK),
            match(LexicalUnit.REPEAT),
            code(),
            match(LexicalUnit.END)
        ));
    }
    
    /**
     * Parses <OUTPUT> -> OUT([VarName])
     */
    private ParseTree outputExpr() throws IOException, ParseException{
        // [23] <Print>  ->  print([VarName])
        ruleOutput(23,"<OUTPUT>","OUT([VarName])");
        return new ParseTree(NonTerminal.Output, Arrays.asList(
            match(LexicalUnit.OUTPUT),
            match(LexicalUnit.LPAREN),
            match(LexicalUnit.VARNAME),
            match(LexicalUnit.RPAREN)
        ));
    }
    
    /**
     * Parses <INPUT> -> IN([VarName])
     */
    private ParseTree inputExpr() throws IOException, ParseException{
        // [24] <Read>  ->  read([VarName])
        ruleOutput(24,"<INPUT>","IN([VarName])");
        return new ParseTree(NonTerminal.Input, Arrays.asList(
            match(LexicalUnit.INPUT),
            match(LexicalUnit.LPAREN),
            match(LexicalUnit.VARNAME),
            match(LexicalUnit.RPAREN)
        ));
    }

/*
    private ParseTree nonterminal() throws IOException, ParseException{
        return new ParseTree(NonTerminal.TODO); // TODO
    }
*/
}
