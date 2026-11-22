// generated with ast extension for cup
// version 0.8
// 9/2/2026 13:36:55


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclMoreConsts extends ConstDeclMore {

    private ConstDeclMore ConstDeclMore;
    private ConstDecl ConstDecl;

    public ConstDeclMoreConsts (ConstDeclMore ConstDeclMore, ConstDecl ConstDecl) {
        this.ConstDeclMore=ConstDeclMore;
        if(ConstDeclMore!=null) ConstDeclMore.setParent(this);
        this.ConstDecl=ConstDecl;
        if(ConstDecl!=null) ConstDecl.setParent(this);
    }

    public ConstDeclMore getConstDeclMore() {
        return ConstDeclMore;
    }

    public void setConstDeclMore(ConstDeclMore ConstDeclMore) {
        this.ConstDeclMore=ConstDeclMore;
    }

    public ConstDecl getConstDecl() {
        return ConstDecl;
    }

    public void setConstDecl(ConstDecl ConstDecl) {
        this.ConstDecl=ConstDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclMore!=null) ConstDeclMore.accept(visitor);
        if(ConstDecl!=null) ConstDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclMore!=null) ConstDeclMore.traverseTopDown(visitor);
        if(ConstDecl!=null) ConstDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclMore!=null) ConstDeclMore.traverseBottomUp(visitor);
        if(ConstDecl!=null) ConstDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclMoreConsts(\n");

        if(ConstDeclMore!=null)
            buffer.append(ConstDeclMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDecl!=null)
            buffer.append(ConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclMoreConsts]");
        return buffer.toString();
    }
}
