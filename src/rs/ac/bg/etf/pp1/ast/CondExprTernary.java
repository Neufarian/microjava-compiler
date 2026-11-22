// generated with ast extension for cup
// version 0.8
// 9/2/2026 13:36:55


package rs.ac.bg.etf.pp1.ast;

public class CondExprTernary extends CondExpr {

    private RelExpr RelExpr;
    private Expr Expr;
    private TernaryThen TernaryThen;
    private TernaryElse TernaryElse;
    private CondExpr CondExpr;

    public CondExprTernary (RelExpr RelExpr, Expr Expr, TernaryThen TernaryThen, TernaryElse TernaryElse, CondExpr CondExpr) {
        this.RelExpr=RelExpr;
        if(RelExpr!=null) RelExpr.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.TernaryThen=TernaryThen;
        if(TernaryThen!=null) TernaryThen.setParent(this);
        this.TernaryElse=TernaryElse;
        if(TernaryElse!=null) TernaryElse.setParent(this);
        this.CondExpr=CondExpr;
        if(CondExpr!=null) CondExpr.setParent(this);
    }

    public RelExpr getRelExpr() {
        return RelExpr;
    }

    public void setRelExpr(RelExpr RelExpr) {
        this.RelExpr=RelExpr;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public TernaryThen getTernaryThen() {
        return TernaryThen;
    }

    public void setTernaryThen(TernaryThen TernaryThen) {
        this.TernaryThen=TernaryThen;
    }

    public TernaryElse getTernaryElse() {
        return TernaryElse;
    }

    public void setTernaryElse(TernaryElse TernaryElse) {
        this.TernaryElse=TernaryElse;
    }

    public CondExpr getCondExpr() {
        return CondExpr;
    }

    public void setCondExpr(CondExpr CondExpr) {
        this.CondExpr=CondExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RelExpr!=null) RelExpr.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(TernaryThen!=null) TernaryThen.accept(visitor);
        if(TernaryElse!=null) TernaryElse.accept(visitor);
        if(CondExpr!=null) CondExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RelExpr!=null) RelExpr.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(TernaryThen!=null) TernaryThen.traverseTopDown(visitor);
        if(TernaryElse!=null) TernaryElse.traverseTopDown(visitor);
        if(CondExpr!=null) CondExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RelExpr!=null) RelExpr.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(TernaryThen!=null) TernaryThen.traverseBottomUp(visitor);
        if(TernaryElse!=null) TernaryElse.traverseBottomUp(visitor);
        if(CondExpr!=null) CondExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondExprTernary(\n");

        if(RelExpr!=null)
            buffer.append(RelExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TernaryThen!=null)
            buffer.append(TernaryThen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TernaryElse!=null)
            buffer.append(TernaryElse.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondExpr!=null)
            buffer.append(CondExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondExprTernary]");
        return buffer.toString();
    }
}
