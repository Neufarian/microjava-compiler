package rs.ac.bg.etf.pp1;

import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	
	private Stack<Integer> ternaryEndStack = new Stack<>();
	private Stack<Integer> ternaryElseStack = new Stack<>();
	
	public int getMainPc() {
		return this.mainPc;
	}
	
	public int getRelOp(Relop node) {
		if (node instanceof RelopEq) {
			return Code.eq;
		}
		else if (node instanceof RelopNeq) {
			return Code.ne;
		}
		else if (node instanceof RelopGt) {
			return Code.gt;
		}
		else if (node instanceof RelopGte) {
			return Code.ge;
		}
		else if (node instanceof RelopLt) {
			return Code.lt;
		}
		else if (node instanceof RelopLte) {
			return Code.le;
		}
		else return -1;
	}
	
	@Override
	public void visit(MethodTypeNameVoid node) {
		node.obj.setAdr(Code.pc);
		if(node.getI1().equalsIgnoreCase("main"))
			this.mainPc = Code.pc;
		
		Code.put(Code.enter);
		Code.put(node.obj.getLevel());
		Code.put(node.obj.getLocalSymbols().size());
	}
	
	@Override
    public void visit(MethodDecl node) {
		Code.put(Code.exit);
		Code.put(Code.return_);
    }
	
	@Override
	public void visit(StatementRead node) {
		if (node.getDesignator().obj.getType().equals(Tab.intType)) Code.put(Code.read);
		else Code.put(Code.bread);
		Code.store(node.getDesignator().obj);
	}
	
	@Override
	public void visit(StatementPrint1 node) {
		Code.loadConst(0);
		if (node.getExpr().struct == Tab.charType) Code.put(Code.bprint);
		else Code.put(Code.print);
	}
	
	@Override
	public void visit(StatementPrint2 node) {
		Code.loadConst(node.getN2());
		if (node.getExpr().struct == Tab.charType) Code.put(Code.bprint);
		else Code.put(Code.print);
	}
	
	@Override
	public void visit(FactorNum node) {
		Code.loadConst(node.getN1());
	}
	
	@Override
	public void visit(FactorChar node) {
		Code.loadConst(node.getC1());
	}
	
	@Override
	public void visit(FactorBool node) {
		Code.loadConst(node.getB1());
	}
	
	@Override
	public void visit(ArrayName node) {
		if (!(node.getParent() instanceof DesignatorEnum))
			Code.load(node.obj);
	}
	
	@Override
	public void visit(FactorNew node) {
		Code.put(Code.newarray);
		if (node.struct == Tab.charType) Code.put(0);
		else Code.put(1);
	}
	
	@Override
	public void visit(FactorDesignator node) {
		if(!(node.getDesignator() instanceof DesignatorLength))
			Code.load(node.getDesignator().obj);
	}
	
	@Override
	public void visit(AddopTermListAddop node) {
		if (node.getAddop() instanceof AddopPlus) Code.put(Code.add);
		else Code.put(Code.sub);
	}
	
	@Override
	public void visit(MulopFactors node) {
		if (node.getMulop() instanceof MulopMul) Code.put(Code.mul);
		else if (node.getMulop() instanceof MulopDiv) Code.put(Code.div);
		else Code.put(Code.rem);
	}
	
	@Override
	public void visit(Assignment node) {
		Code.store(node.getDesignator().obj);
	}
	
	@Override
	public void visit(Increment node) {
		if(node.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		Code.load(node.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(node.getDesignator().obj);
	}
	
	@Override
	public void visit(Decrement node) {
		if(node.getDesignator().obj.getKind() == Obj.Elem)
			Code.put(Code.dup2);
		Code.load(node.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(node.getDesignator().obj);
	}
	
	@Override
	public void visit(FactorMinus node) {
		Code.put(Code.neg);
	}
	
	@Override
	public void visit(ExprLen node) {
		Code.put(Code.arraylength);
	}
	
	@Override
	public void visit(DesignatorLength node) {
	    Code.put(Code.arraylength);
	}
	
	@Override
	public void visit(TernaryElse node) {
		Code.fixup(ternaryElseStack.pop());	
	}
	
	@Override
	public void visit(CondExprTernary node) {
		Code.fixup(ternaryEndStack.pop());
	}
	
	@Override
	public void visit(TernaryThen node) {
	    Code.putJump(0);
	    ternaryEndStack.push(Code.pc - 2);
	}
	
	@Override
	public void visit(RelExprAdd node) {
	    if (node.getParent() instanceof CondExprTernary
	        && !(node.getAddExpr().getTerm().getFactor() instanceof FactorExpr)) {

	        Code.loadConst(0);
	        Code.putFalseJump(Code.ne, 0);
	        ternaryElseStack.push(Code.pc - 2);
	    }
	}
	
	@Override
	public void visit(RelExprRelop node) {
	    Code.putFalseJump(getRelOp(node.getRelop()), 0);
	    ternaryElseStack.push(Code.pc - 2);
	}
	
	

}
