import java.io.*;
import java.util.*;


public class Main{
    /**
     *
     * The Analyzer for Gillis Program
     *
     * @param args  The path of the file that contains a Gillis Program to give to the analyzer
     * @throws IOException java.io.IOException
     *
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.out.println("Please use this format:");
            System.out.println("java -jar dest/part1.jar test/file.gls");
            System.exit(0);
        }
        // Creating the file using the filePath
        File file = new File(args[0]);
        FileReader fileData = null;
        // checking if the file exists
        if (file.exists()){
            fileData = new FileReader(file);
        }else{
            System.out.println("Please give a valid file for file.gls");
            System.exit(0);
        }
        // give the data of the file to the analyzer
        final LexicalAnalyzer analyzer = new LexicalAnalyzer(fileData);
        // dictionnary used to store variable inside
        Map<String, Symbol> dictionnary = new TreeMap<>();
        // symbol represents the currently read symbol
        Symbol symbol = null;
        // We iterate while we do not reach the end of the file (marked by EOS)
        while(!(symbol = analyzer.nextToken()).getType().equals(LexicalUnit.EOS)){
            System.out.println(symbol.toString());
            // If it is a variable, add it to the table
            if(symbol.getType().equals(LexicalUnit.VARNAME)){
                if(!dictionnary.containsKey(symbol.getValue().toString())){
                    dictionnary.put(symbol.getValue().toString(),symbol);
                }
            }
        }
        System.out.println("\nVariables");
        // Print the variables
        for(Map.Entry<String, Symbol> variable : dictionnary.entrySet())
            System.out.println(variable.getKey()+"\t"+(variable.getValue().getLine()));
    }
}
