/**
 * A non-terminal symbol, a.k.a. a variable in the grammar.
 */
public enum NonTerminal {
    /** &lt;Program&gt; */
    Program,
    /** &lt;Code&gt; */
    Code,
    /** &lt;Instruction&gt; */
    Instruction,
    /** &lt;Assign&gt; */
    Assign,
    /** &lt;ExprArith&gt; */
    ExprArith,
    /** &lt;Op&gt; */
    Op,
    /** &lt;If&gt; */
    If,
    /** &lt;Cond&gt; */
    Cond,
    /** &lt;Comp&gt; */
    Comp,
    /** &lt;While&gt; */
    While,
    /** &lt;Output&gt; */
    Output,
    /** &lt;Input&gt; */
    Input;
    
    /**
     * Returns a string representation of the non-terminal (without the surrounding &lt;&nbsp;&gt;).
     * 
     * @return a String representing the non-terminal.
     */
    @Override
    public String toString() {
        String n=this.name();
        String realName=n;
        return realName;
    }
    
    /**
     * Returns th LaTeX code to represent the non-terminal.
     * 
     * The non-terminal is in sans-serif font and surrounded by angle brackets.
     * 
     * @return a String representing LaTeX code for the non-terminal.
     */
    public String toTexString() {
        return "\\textsf{$\\langle$"+this.toString()+"$\\rangle$}";
    }
}
