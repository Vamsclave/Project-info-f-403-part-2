
import java.util.regex.PatternSyntaxException;

/**
 *
 * Scanner class, generated by JFlex.
 * Function nextToken is the important one as it reads the file and returns the next matched toke.
 *
 */

%%// Options of the scanner

%class LexicalAnalyzer // Name
%unicode               // Use unicode
%line                  // Use line counter (yyline variable)
%column                // Use character counter by line (yycolumn variable)
%function nextToken
%type Symbol
%yylexthrow PatternSyntaxException

%eofval{
	return new Symbol(LexicalUnit.EOS, yyline, yycolumn);
%eofval}

//Extended Regular Expressions

AlphaUpperCase    = [A-Z]
AlphaLowerCase    = [a-z]
Alpha             = {AlphaUpperCase}|{AlphaLowerCase}
Numeric           = [0-9]
AlphaNumeric      = {Alpha}|{Numeric}
// LowerAlphaNumeric = {AlphaLowerCase}|{Numeric}

BadInteger     = (0[0-9]+)
Integer        = ([1-9][0-9]*)|0
ProgName       = {AlphaUpperCase}({Alpha}|"_")*
VarName        = ({AlphaLowerCase})({AlphaNumeric})*
LineFeed       = "\n"
CarriageReturn = "\r"
EndLine        = ({LineFeed}{CarriageReturn}?) | ({CarriageReturn}{LineFeed}?)
Space          = (\t | \f | " ")
Spaces         = {Space}+
Separator      = ({Spaces}) | ({EndLine}) // Was Space (no 's') was it a typo? (did produce a warning)
Any            = ([^"\n""\r"])*
UpToEnd        = ({Any}{EndLine}) | ({EndLine})

//Declare exclusive states
%xstate YYINITIAL, SHORTCOMMENTS, LONGCOMMENTS

%%// Identification of tokens


<LONGCOMMENTS> {
// End of comment
	"!!"			{yybegin(YYINITIAL);} // go back to analysis
  <<EOF>>          {throw new PatternSyntaxException("A comment is never closed.",yytext(),yyline);}
	[^]					     {} //ignore any character
}

<YYINITIAL> {
// Comments
    "!!"              {yybegin(LONGCOMMENTS);} // go to ignore mode
    "$"{UpToEnd}     {} // go to ignore mode
// Code delimiters
  "LET"             {return new Symbol(LexicalUnit.LET, yyline, yycolumn, yytext());}
  "BE"              {return new Symbol(LexicalUnit.BE, yyline, yycolumn, yytext());}
  "END"             {return new Symbol(LexicalUnit.END, yyline, yycolumn, yytext());}
  ":"               {return new Symbol(LexicalUnit.COLUMN, yyline, yycolumn, yytext());}
// Assignation
  "="                {return new Symbol(LexicalUnit.ASSIGN, yyline, yycolumn, yytext());}
// Parenthesis
  "("                 {return new Symbol(LexicalUnit.LPAREN, yyline, yycolumn, yytext());}
  ")"                 {return new Symbol(LexicalUnit.RPAREN, yyline, yycolumn, yytext());}
// Brackets
  "{"                 {return new Symbol(LexicalUnit.LBRACK, yyline, yycolumn, yytext());}
  "}"                 {return new Symbol(LexicalUnit.RBRACK, yyline, yycolumn, yytext());}
  "|"                 {return new Symbol(LexicalUnit.PIPE, yyline, yycolumn, yytext());}
// Arithmetic signs
  "+"                 {return new Symbol(LexicalUnit.PLUS, yyline, yycolumn, yytext());}
  "-"                 {return new Symbol(LexicalUnit.MINUS, yyline, yycolumn, yytext());}
  "*"                 {return new Symbol(LexicalUnit.TIMES, yyline, yycolumn, yytext());}
  "/"                 {return new Symbol(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());}
// Logical operators
  "->"               {return new Symbol(LexicalUnit.IMPLIES, yyline, yycolumn, yytext());}
// Conditional keywords
  "IF"                {return new Symbol(LexicalUnit.IF, yyline, yycolumn, yytext());}
  "THEN"              {return new Symbol(LexicalUnit.THEN, yyline, yycolumn, yytext());}
  "ELSE"              {return new Symbol(LexicalUnit.ELSE, yyline, yycolumn, yytext());}
// Loop keywords
  "WHILE"             {return new Symbol(LexicalUnit.WHILE, yyline, yycolumn, yytext());}
  "REPEAT"            {return new Symbol(LexicalUnit.REPEAT, yyline, yycolumn, yytext());}
// Comparison operators
  "=="                {return new Symbol(LexicalUnit.EQUAL, yyline, yycolumn, yytext());}
  "<="                {return new Symbol(LexicalUnit.SMALEQ, yyline, yycolumn, yytext());}
  "<"                 {return new Symbol(LexicalUnit.SMALLER, yyline, yycolumn, yytext());}
// IO keywords
  "OUT"             {return new Symbol(LexicalUnit.OUTPUT, yyline, yycolumn, yytext());}
  "IN"              {return new Symbol(LexicalUnit.INPUT, yyline, yycolumn, yytext());}
// Numbers
  {BadInteger}        {System.err.println("Warning! Numbers with leading zeros are deprecated: " + yytext()); return new Symbol(LexicalUnit.NUMBER, yyline, yycolumn, Integer.valueOf(yytext()));}
  {Integer}           {return new Symbol(LexicalUnit.NUMBER, yyline, yycolumn, Integer.valueOf(yytext()));}
  {ProgName}           {return new Symbol(LexicalUnit.PROGNAME,yyline, yycolumn,yytext());}
  {VarName}           {return new Symbol(LexicalUnit.VARNAME,yyline, yycolumn,yytext());}
  {Separator}         {}// ignore spaces
  [^]                 {throw new PatternSyntaxException("Unmatched token, out of symbols",yytext(),yyline);} // unmatched token gives an error
}