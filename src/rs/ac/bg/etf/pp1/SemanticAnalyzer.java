package rs.ac.bg.etf.pp1;

import java.util.HashSet;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

    /* ============================================================ */
    /* ======================== FIELDS ============================ */
    /* ============================================================ */

    private boolean errorDetected = false;
    
    private HashSet<String> declaredMethods = new HashSet<>();

    private Obj mainMethod = null;
    private Obj currentProgram;
    private Obj currentMethod;
    private Obj currentEnum;

    private Struct currentType;
    private Struct constantType;

    private int constant;
    private int enumValueCounter;
    int nVars;

    private final Struct boolType = Tab.find("bool").getType();
    private final Logger log = Logger.getLogger(getClass());


    /* ============================================================ */
    /* ===================== ERROR HANDLING ======================= */
    /* ============================================================ */

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.info(msg.toString());
    }

    public boolean passed() {
        return !errorDetected;
    }
    
    /* ============================================================ */
    /* ===================== HELPER FUNCTIONS ===================== */
    /* ============================================================ */
    
    private boolean isIntOrEnum(Struct t) {
        return t == Tab.intType || t.getKind() == Struct.Enum;
    }

    /* ============================================================ */
    /* ===================== PROGRAM SECTION ====================== */
    /* ============================================================ */

    @Override
    public void visit(ProgName node) {
        currentProgram = Tab.insert(Obj.Prog, node.getI1(), Tab.noType);
        Tab.openScope();
    }

    @Override
    public void visit(Program node) {
    	nVars = Tab.currentScope.getnVars();
        Tab.chainLocalSymbols(currentProgram);
        Tab.closeScope();
        currentProgram = null;
        if (mainMethod == null || mainMethod.getLevel() > 0) 
        	report_error("Program mora imati adekvatnu main metodu!", null);
    }

    /* ============================================================ */
    /* ========================== TYPE ============================ */
    /* ============================================================ */

    @Override
    public void visit(Type node) {
        Obj typeObj = Tab.find(node.getI1());

        if (typeObj == Tab.noObj) {
            report_error("Nepoznati tip podatka: " + node.getI1(), node);
            node.struct = currentType = Tab.noType;
        }
        else if (typeObj.getKind() != Obj.Type) {
            report_error("Neadekvatni tip podatka: " + node.getI1(), node);
            node.struct = currentType = Tab.noType;
        }
        else {
        	node.struct =  currentType = typeObj.getType();
        }
    }

    /* ============================================================ */
    /* ======================= CONSTANTS ========================== */
    /* ============================================================ */

    @Override
    public void visit(ConstTypeNum node) {
        constant = node.getN1();
        constantType = Tab.intType;
    }

    @Override
    public void visit(ConstTypeChar node) {
        constant = node.getC1();
        constantType = Tab.charType;
    }

    @Override
    public void visit(ConstTypeBool node) {
        constant = node.getB1();
        constantType = boolType;
    }

    @Override
    public void visit(ConstDecl node) {

        Obj found = Tab.find(node.getI1());

        if (found != Tab.noObj) {
            report_error("Dvostruka definicija konstante: " + node.getI1(), node);
            return;
        }

        if (!constantType.assignableTo(currentType)) {
            report_error("Neadekvatna dodela vrednosti konstanti: " + node.getI1(), node);
            return;
        }

        Obj constObj = Tab.insert(Obj.Con, node.getI1(), currentType);
        constObj.setAdr(constant);
        report_info("Definisana konstanta: " + node.getI1(), node);
    }

    /* ============================================================ */
    /* ======================= VARIABLES ========================== */
    /* ============================================================ */

    @Override
    public void visit(VarDeclNoArray node) {

        String name = node.getI1();
        Obj varObj = null;
        if (currentMethod != null) varObj = Tab.currentScope().findSymbol(name);
        else varObj = Tab.find(name);

        if (varObj != null && varObj != Tab.noObj) {
            report_error("Dvostruka definicija promenljive: " + name, node);
            return;
        }

        Tab.insert(Obj.Var, name, currentType);
        report_info("Definisana promenljiva: " + node.getI1(), node);
    }

    @Override
    public void visit(VarDeclArray node) {

        String name = node.getI1();
        Obj varObj = null;
        if (currentMethod != null) varObj = Tab.currentScope().findSymbol(name);
        else varObj = Tab.find(name);

        if (varObj != null && varObj != Tab.noObj) {
            report_error("Dvostruka definicija promenljive: " + name, node);
            return;
        }

        Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
        report_info("Definisana promenljiva: " + node.getI1(), node);
    }

    /* ============================================================ */
    /* ========================== ENUM ============================ */
    /* ============================================================ */

    @Override
    public void visit(EnumName node) {

        Obj enumObj = Tab.find(node.getI1());

        if (enumObj != Tab.noObj) {
            report_error("Dvostruka definicija nabrajanja: " + node.getI1(), node);
            return;
        }

        currentEnum = Tab.insert(Obj.Type, node.getI1(), new Struct(Struct.Enum));
        enumValueCounter = 0;
    }

    @Override
    public void visit(EnumDeclNoValue node) {
        Struct enumStruct = currentEnum.getType();

        if (enumStruct.getMembersTable().searchKey(node.getI1()) != null) {
            report_error("Duplikat u enum-u: " + node.getI1(), node);
            return;
        }

        Obj con = new Obj(Obj.Con, node.getI1(), enumStruct);
        con.setAdr(enumValueCounter++);

        enumStruct.getMembersTable().insertKey(con);
    }

    @Override
    public void visit(EnumDeclValue node) {
        Struct enumStruct = currentEnum.getType();

        if (enumStruct.getMembersTable().searchKey(node.getI1()) != null) {
            report_error("Duplikat u enum-u: " + node.getI1(), node);
            return;
        }

        Obj con = new Obj(Obj.Con, node.getI1(), enumStruct);
        enumValueCounter = node.getN2();
        con.setAdr(enumValueCounter++);

        enumStruct.getMembersTable().insertKey(con);
    }

    /* ============================================================ */
    /* ========================== METHODS ========================= */
    /* ============================================================ */

    @Override
    public void visit(MethodTypeNameType node) {
    	if (declaredMethods.contains(node.getI2())) {
    		report_error("Dvostruka definicija metode: " + node.getI2(), node);
    	}
    	node.obj = currentMethod = Tab.insert(Obj.Meth, node.getI2(), currentType);
        declaredMethods.add(node.getI2());
        Tab.openScope();
    }

    @Override
    public void visit(MethodTypeNameVoid node) {
    	if (declaredMethods.contains(node.getI1())) {
    		report_error("Dvostruka definicija metode: " + node.getI1(), node);
    	}
    	node.obj = currentMethod = Tab.insert(Obj.Meth, node.getI1(), Tab.noType);
        if (node.getI1().equalsIgnoreCase("main")) mainMethod = currentMethod;
        declaredMethods.add(node.getI1());
        Tab.openScope();
    }

    @Override
    public void visit(MethodDecl node) {
        Tab.chainLocalSymbols(currentMethod);
        Tab.closeScope();
        currentMethod = null;
    }

    /* ============================================================ */
    /* ========================== FACTOR ========================== */
    /* ============================================================ */

    @Override
    public void visit(FactorMinus node) {
    	if (node.getFactor().struct != Tab.intType) {
    		report_error("Dozvoljeno je negiranje samo int tipova!", node);
    		node.struct = Tab.noType;
    	}
    	else node.struct = Tab.intType;
    }
    
    @Override
    public void visit(FactorNum node) {
        node.struct = Tab.intType;
    }

    @Override
    public void visit(FactorChar node) {
        node.struct = Tab.charType;
    }

    @Override
    public void visit(FactorExpr node) {
        node.struct = node.getExpr().struct;
    }
    
    @Override
    public void visit(FactorBool node) {
    	node.struct = boolType;
    }

    @Override
    public void visit(FactorDesignator node) {
        node.struct = node.getDesignator().obj.getType();
    }
    
    @Override
    public void visit(FactorNew node) {
    	if (node.getExpr().struct != Tab.intType) {
    		report_error("Izraz mora biti tipa int!", node);
    		node.struct = Tab.noType;
    		return;
    	}
    	else node.struct = new Obj(Obj.Var, "array", new Struct(Struct.Array, currentType)).getType();
    }
    
    /* ============================================================ */
    /* =========================== TERM =========================== */
    /* ============================================================ */

    @Override
    public void visit(Term node) {

        Struct factorType = node.getFactor().struct;
        Struct listType = node.getMulopFactorList().struct;

        if (listType == Tab.noType) {
            node.struct = factorType;
            return;
        }

        if (isIntOrEnum(factorType) && isIntOrEnum(listType))
            node.struct = Tab.intType;
        else {
            report_error("Mul operator zahteva int operande!", node);
            node.struct = Tab.noType;
        }
    }
    
    /* ============================================================ */
    /* ======================== EXPRESSIONS ======================= */
    /* ============================================================ */

    @Override
    public void visit(AddExpr node) {

        Struct termType = node.getTerm().struct;
        Struct listType = node.getAddopTermList().struct;

        if (listType == Tab.noType) {
            node.struct = termType;
            return;
        }

        if (isIntOrEnum(termType) && isIntOrEnum(listType))
            node.struct = Tab.intType;
        else {
            report_error("Add operator zahteva int operande!", node);
            node.struct = Tab.noType;
        }
    }

    @Override
    public void visit(RelExprRelop node) {

        if (!node.getAddExpr().struct.compatibleWith(node.getAddExpr1().struct)) {
            report_error("Tipovi oba izraza moraju biti kompatibilni!", node);
            node.struct = Tab.noType;
            return;
        }
        
        if (node.getAddExpr().struct.getKind() == Struct.Array) {
        	if (!(node.getRelop() instanceof RelopEq) && !(node.getRelop() instanceof RelopNeq)) {
        		report_error("Nizovima se moze ispitivati samo jednakost!", node);
        		node.struct = Tab.noType;
                return;
        	}
        }
        
        node.struct = node.getAddExpr().struct;
    }

    @Override
    public void visit(RelExprAdd node) {
        node.struct = node.getAddExpr().struct;
    }

    @Override
    public void visit(CondExprRel node) {
        node.struct = node.getRelExpr().struct;
    }
    
    @Override
    public void visit(CondExprTernary node) {
    	if (!(isIntOrEnum(node.getExpr().struct) && isIntOrEnum(node.getCondExpr().struct)) &&
    			node.getExpr().struct != node.getCondExpr().struct) {
    		report_error("Tipovi izraza moraju da budu isti!", node);
    		node.struct = Tab.noType;
    		return;
    	}
    	node.struct = node.getExpr().struct;
    }

    @Override
    public void visit(ExprCond node) {
        node.struct = node.getCondExpr().struct;
    }

    @Override
    public void visit(AddopTermListAddop node) {
        Struct listType = node.getAddopTermList().struct;
        Struct termType = node.getTerm().struct;

        if (listType != Tab.noType) {
	        if (isIntOrEnum(termType) && isIntOrEnum(listType))
	            node.struct = Tab.intType;
	        else {
	            report_error("Add operator zahteva int operande!", node);
	            node.struct = Tab.noType;
	        }
	        if (!listType.compatibleWith(termType)) {
	            report_error("Tipovi izraza moraju biti kompatibilni!", node);
	            node.struct = Tab.noType;
	        }
        } 
        else node.struct = termType;
    }

    @Override
    public void visit(AddopTermListEpsilon node) {
        node.struct = Tab.noType;
    }

    @Override
    public void visit(MulopFactors node) {
        Struct listType = node.getMulopFactorList().struct;
        Struct termType = node.getFactor().struct;

        if (listType != Tab.noType) {
	        if (isIntOrEnum(termType) && isIntOrEnum(listType))
	            node.struct = Tab.intType;
	        else {
	            report_error("Add operator zahteva int operande!", node);
	            node.struct = Tab.noType;
	        }
        } 
        else node.struct = termType;
    }

    @Override
    public void visit(NoMulopFactor node) {
        node.struct = Tab.noType;
    }
    
    @Override
    public void visit(ExprOrd node) {
    	if (node.getExpr().struct != Tab.charType) {
    		report_error("Metoda ord() zahteva char operand!", node);
            node.struct = Tab.noType;
    	}
    	else node.struct = Tab.intType; 
    }
    
    @Override
    public void visit(ExprChr node) {
    	if (node.getExpr().struct != Tab.intType) {
    		report_error("Metoda chr() zahteva int operand!", node);
            node.struct = Tab.noType;
    	}
    	else node.struct = Tab.charType; 
    }

    @Override
    public void visit(ExprLen node) {
    	if (node.getExpr().struct.getKind() == Struct.Array && 
    		(node.getExpr().struct.getElemType() == Tab.intType ||
    		node.getExpr().struct.getElemType() == Tab.charType))
    		node.struct = Tab.intType; 
    	else {
    		report_error("Metoda len() zahteva niz ili znakovni niz!", node);
            node.struct = Tab.noType;
    	}
    }

    /* ============================================================ */
    /* ======================== DESIGNATOR ======================== */
    /* ============================================================ */

    @Override
    public void visit(DesignatorIdent node) {
        String name = node.getI1();
        Obj designObj = null;

        if (currentMethod != null) designObj = Tab.currentScope().findSymbol(name);
        if (designObj == null) designObj = Tab.find(name);

        if ((designObj == null || designObj == Tab.noObj) && declaredMethods.contains(name)) {
            designObj = new Obj(Obj.Meth, name, null);
        }

        if (designObj == null || designObj == Tab.noObj) {
            report_error("Nedeklarisana promenljiva ili metoda: " + name, node);
            node.obj = Tab.noObj;
        } else {
            node.obj = designObj;
        }
    }

    @Override
    public void visit(DesignatorExpr node) {

    	String name = node.getArrayName().getI1();
        Obj arrayObj = null;
        if (currentMethod != null) arrayObj = Tab.currentScope().findSymbol(name);
        if (arrayObj == null) arrayObj = Tab.find(name);

        if (arrayObj == null || arrayObj == Tab.noObj) {
            report_error("Nedeklarisan niz: " + name, node);
            node.obj = Tab.noObj;
            return;
        }

        if (arrayObj.getType().getKind() != Struct.Array) {

            report_error("Promenljiva nije niz: " + name, node);
            node.obj = Tab.noObj;
            return;
        }

        if (!isIntOrEnum(node.getExpr().struct))
            report_error("Indeks mora biti int!", node);

        node.obj = new Obj(
                Obj.Elem,
                arrayObj.getName(),
                arrayObj.getType().getElemType()
        );
    }
    
    @Override
    public void visit(ArrayName node) {
    	node.obj = Tab.find(node.getI1());
    }
    
    @Override
    public void visit(DesignatorLength node) {
        Obj arrayObj = Tab.find(node.getArrayName().getI1());

        if (arrayObj == Tab.noObj) {
            report_error("Nedeklarisan niz!", node);
            node.obj = Tab.noObj;
            return;
        }

        if (arrayObj.getType().getKind() != Struct.Array) {
            report_error("Promenljiva nije niz!", node);
        }

        node.obj = new Obj(Obj.Con, "len", Tab.intType);
    }

    @Override
    public void visit(DesignatorEnum node) {
        Obj enumObj = Tab.find(node.getArrayName().getI1());

        if (enumObj == Tab.noObj || enumObj.getKind() != Obj.Type
                || enumObj.getType().getKind() != Struct.Enum) {
            report_error("Ime nije enum: " + node.getArrayName().getI1(), node);
            node.obj = Tab.noObj;
            return;
        }

        Struct enumStruct = enumObj.getType();

        Obj con = enumStruct.getMembersTable().searchKey(node.getI2());

        if (con == null) {
            report_error("Enum nema vrednost: " + node.getI2(), node);
            node.obj = Tab.noObj;
            return;
        }

        node.obj = con;
    }

    /* ============================================================ */
    /* =================== DESIGNATOR STATEMENT =================== */
    /* ============================================================ */

    @Override
    public void visit(Assignment node) {

        Obj designObj = node.getDesignator().obj;

        if (designObj.getKind() != Obj.Var &&
            designObj.getKind() != Obj.Elem) {

            report_error("Leva strana dodele mora biti promenljiva ili element niza!", node);
        }

        Struct exprType = node.getExpr().struct;
        Struct destType = designObj.getType();

        if (isIntOrEnum(exprType) && isIntOrEnum(destType)) return;
        
        if (exprType.equals(Tab.noType) || !exprType.assignableTo(destType)) {
           report_error("Nekompatibilna dodela!", node);
        }
    }
    
    @Override
    public void visit(Increment node) {
    		
    	Obj designObj = node.getDesignator().obj;

    	if ((designObj.getKind() != Obj.Var || designObj.getType().getKind() == Struct.Array) && designObj.getKind() != Obj.Elem) {
    	    report_error("Operand mora biti promenljiva ili element niza!", node);
    	    return;
    	}

    	if (designObj.getType() != Tab.intType) {
    	    report_error("Operand inkrementa mora biti int!", node);
    	}
        
    }
    
    @Override
    public void visit(Decrement node) {
    		
    	Obj designObj = node.getDesignator().obj;

    	if ((designObj.getKind() != Obj.Var || designObj.getType().getKind() == Struct.Array) && designObj.getKind() != Obj.Elem) {
    	    report_error("Operand mora biti promenljiva ili element niza!", node);
    	    return;
    	}

    	if (designObj.getType() != Tab.intType) {
    	    report_error("Operand inkrementa mora biti int!", node);
    	}
        
    }
    
    @Override
    public void visit(MethCallNoParams node) {
        Obj obj = node.getDesignator().obj;

        if (obj == Tab.noObj) {
            String name = node.getDesignator() instanceof DesignatorIdent
                          ? ((DesignatorIdent) node.getDesignator()).getI1()
                          : "<nepoznato>";
            report_error("Nedeklarisana metoda: " + name, node);
            return;
        }

        if (obj.getKind() != Obj.Meth) {
            report_error("Designator nije metoda: " + obj.getName(), node);
            return;
        }

    }
    
    @Override
    public void visit(MethCallWithParams node) {
        Obj obj = node.getDesignator().obj;

        if (obj == Tab.noObj) {
            String name = node.getDesignator() instanceof DesignatorIdent
                          ? ((DesignatorIdent) node.getDesignator()).getI1()
                          : "<nepoznato>";
            report_error("Nedeklarisana metoda: " + name, node);
            return;
        }

        if (obj.getKind() != Obj.Meth) {
            report_error("Designator nije metoda: " + obj.getName(), node);
            return;
        }

    }
    
    /* ============================================================ */
    /* ======================== STATEMENTS ======================== */
    /* ============================================================ */
    
    @Override
    public void visit(StatementRead node) {
    	Obj designObj = node.getDesignator().obj;

    	if ((designObj.getKind() != Obj.Var || designObj.getType().getKind() == Struct.Array) && designObj.getKind() != Obj.Elem) {
    	    report_error("Operand mora biti promenljiva ili element niza!", node);
    	    return;
    	}

    	if (designObj.getType() != Tab.intType &&
    		designObj.getType() != Tab.charType &&
    		designObj.getType() != boolType) {
    	    report_error("Operand read metode mora biti tipa int/char/bool!", node);
    	}
    }
    
    @Override
    public void visit(StatementPrint1 node) {
    	Struct exprType = node.getExpr().struct;
    	if (exprType!= Tab.intType &&
    		exprType != Tab.charType &&
    		exprType != boolType &&
    		exprType.getKind() != Struct.Class) {
    		report_error("Izraz unutar print metode mora biti tipa int/char/bool!", node);
    	}
    }
    
    @Override
    public void visit(StatementPrint2 node) {
    	Struct exprType = node.getExpr().struct;
    	if (exprType!= Tab.intType &&
    		exprType != Tab.charType &&
    		exprType != boolType &&
    		exprType.getKind() != Struct.Class) {
    		report_error("Izraz unutar print metode mora biti tipa int/char/bool!", node);
    	}
    }
    
    /* ============================================================ */
    /* ========================= FORMPARS ========================= */
    /* ============================================================ */
    
    @Override
    public void visit(FormParDeclNoArray node) {

        String name = node.getI2();
        Obj varObj = Tab.currentScope.findSymbol(name);

        if (varObj != null) {
            report_error("Dvostruka definicija formalnog parametra: " + name, node);
            return;
        }

        varObj = Tab.insert(Obj.Var, name, currentType);
        varObj.setFpPos(1);
        currentMethod.setLevel(currentMethod.getLevel() + 1);
    }

    @Override
    public void visit(FormParDeclArray node) {

        String name = node.getI2();
        Obj varObj = Tab.currentScope.findSymbol(name);

        if (varObj != null) {
            report_error("Dvostruka definicija formalnog parametra: " + name, node);
            return;
        }

        varObj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, currentType));
        varObj.setFpPos(1);
        currentMethod.setLevel(currentMethod.getLevel() + 1);
    }
    
}