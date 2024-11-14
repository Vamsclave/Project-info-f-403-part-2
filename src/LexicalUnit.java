/**
 * A terminal symbol, a.k.a. a letter in the grammar.
 */
public enum LexicalUnit{
    /** [ProgName] */
    PROGNAME,
    /** [VarName] */
    VARNAME,
    /** [Number] */
    NUMBER,
    /** <code>LET</code> */
    LET,
    /** <code>BE</code> */
    BE,
    /** <code>END</code> */
    END,
    /** <code>:</code> */
    COLUMN,
    /** <code>=</code> */
    ASSIGN,
    /** <code>(</code> */
    LPAREN,
    /** <code>)</code> */
    RPAREN,
    /** <code>-</code> */
    MINUS,
    /** <code>+</code> */
    PLUS,
    /** <code>*</code> */
    TIMES,
    /** <code>/</code> */
    DIVIDE,
    /** <code>IF</code> */
    IF,
    /** <code>THEN</code> */
    THEN,
    /** <code>ELSE</code> */
    ELSE,
    /** <code>{</code> */
    LBRACK,
    /** <code>}</code> */
    RBRACK,
    /** <code>-></code> */
    IMPLIES,
    /** <code>|</code> */
    PIPE,
    /** <code>==</code> */
    EQUAL,
    /** <code>&lt;=</code> */
    SMALEQ,
    /** <code>&lt;</code> */
    SMALLER,
    /** <code>WHILE</code> */
    WHILE,
    /** <code>REPEAT</code> */
    REPEAT,
    /** <code>OUT</code> */
    OUTPUT,
    /** <code>IN</code> */
    INPUT,
    /** <code>ε</code> */
    EPSILON,
    /** End Of Stream */
    EOS; // End of stream

    /**
     * Returns the representation the terminal.
     * 
     * @return a String containing the terminal type (word or abstract expression).
     */
    @Override
    public String toString() {
        String n=this.name();
        switch (this) {
            case PROGNAME:
                n="[Progname]";
                break;
            case VARNAME:
                n="[VarName]";
                break;
            case NUMBER:
                n="[Number]";
                break;
            case LET:
                n="[LET]";
                break;    
            case BE:
                n="BE";
                break;
            case END:
                n="END";
                break;
            case COLUMN:
                n=":";
                break;    
            case ASSIGN:
                n="=";
                break;
            case LPAREN:
                n="(";
                break;
            case RPAREN:
                n=")";
                break;
            case MINUS:
                n="-";
                break;
            case PLUS:
                n="+";
                break;
            case TIMES:
                n="*";
                break;
            case DIVIDE:
                n="/";
                break;
            case IF:
                n="IF";
                break;
            case THEN:
                n="THEN";
                break;
            case ELSE:
                n="ELSE";
                break;
            case LBRACK:
                n="{";
                break;
            case RBRACK:
                n="}";
                break;
            case IMPLIES:
                n="->";
                break;
            case PIPE:
                n="|";
                break;
            case EQUAL:
                n="==";
                break;
            case SMALEQ:
                n="=<";
                break;
            case SMALLER:
                n="<";
                break;
            case WHILE:
                n="WHILE";
                break;
            case REPEAT:
                n="REPEAT";
                break;
            case OUTPUT:
                n="OUT";
                break;
            case INPUT:
                n="INPUT";
                break;
            case EOS:
                n="EOS";
                break;
            case EPSILON:
                n="/epsilon/";
                break;
        }
        return n;
    }
    
    
    /**
     * Returns the LaTeX code representing the terminal.
     * 
     * @return a String containing the LaTeX code for the terminal.
     */
    public String toTexString() {
        String n=this.name();
        switch (this) {
            case PROGNAME:
                n="ProgName";
                break;
            case VARNAME:
                n="VarName";
                break;
            case NUMBER:
                n="Number";
                break;
            case LET:
                n="\\texttt{LET}";
                break;
            case BE:
                n="\\texttt{BE}";
                break;
            case END:
                n="\\texttt{END}";
                break;
            case ASSIGN:
                n="\\texttt{=}";
                break;
            case LPAREN:
                n="\\texttt{(}";
                break;
            case RPAREN:
                n="\\texttt{)}";
                break;
            case MINUS:
                n="\\texttt{-}";
                break;
            case PLUS:
                n="\\texttt{+}";
                break;
            case TIMES:
                n="\\texttt{*}";
                break;
            case DIVIDE:
                n="\\texttt{/}";
                break;
            case IF:
                n="\\texttt{if}";
                break;
            case THEN:
                n="\\texttt{then}";
                break;
            case ELSE:
                n="\\texttt{else}";
                break;
            case LBRACK:
                n="\\texttt{\\{}";
                break;
            case RBRACK:
                n="\\texttt{\\}}";
                break;
            case IMPLIES:
                n="\\texttt{->}";
                break;
            case PIPE:
                n="\\texttt{|}";
                break;
            case EQUAL:
                n="\\texttt{==}";
                break;
            case SMALEQ:
                n="\\texttt{=<}";
                break;
            case SMALLER:
                n="\\texttt{<}";
                break;
            case WHILE:
                n="\\texttt{WHILE}";
                break;
            case REPEAT:
                n="\\texttt{REPEAT}";
                break;
            case OUTPUT:
                n="\\texttt{OUT}";
                break;
            case INPUT:
                n="\\texttt{IN}";
                break;
            case EOS:
                n="EOS";
                break;
            case EPSILON:
                n="$\\varepsilon$";
                break;
            default:
                break;
        }
        return n;
    }
}
