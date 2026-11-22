package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		if (args.length < 2) {
			log.error("Upotreba: Compiler <source.mj> <output.obj>");
			return;
		}
		
		Reader br = null;
		
		try {
			File sourceCode = new File(args[0]);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			/* Formiranje AST */
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse();
			
			Program prog = (Program)(s.value);
			
			/* Ispis AST */
			log.info(prog.toString(""));
			
			/* Inicijalizacija tabele simbola */
			Tab.init();
			Struct boolType = new Struct(Struct.Bool);
			Obj boolObj = Tab.insert(Obj.Type, "bool", boolType);
			boolObj.setAdr(-1);
			boolObj.setLevel(-1);
			
			/* Semanticka analiza*/
			System.out.println("=====================SEMANTIC ANALYSIS=========================");
			SemanticAnalyzer sa = new SemanticAnalyzer();
			prog.traverseBottomUp(sa);
			
			/* Ispis tabele simbola*/
			Tab.dump();
			
			if (!p.errorDetected && sa.passed()) {
				/* Generisanje koda */
				File objFile = new File(args[1]);
				if (objFile.exists()) objFile.delete();
				
				CodeGenerator cg = new CodeGenerator();
				prog.traverseBottomUp(cg);
				Code.dataSize = sa.nVars;
				Code.mainPc = cg.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Generisanje uspesno zavrseno!");
			}
			else log.info("Parsiranje NIJE uspesno zavrseno!");
			
		}
		finally {
			if (br != null) try { br.close(); } catch (IOException e) { log.error(e.getMessage(), e); }
		}
	}
}
