// generated with ast extension for cup
// version 0.8
// 9/2/2026 13:36:55


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclMoreEnums extends EnumDeclMore {

    private EnumDeclMore EnumDeclMore;
    private EnumDecl EnumDecl;

    public EnumDeclMoreEnums (EnumDeclMore EnumDeclMore, EnumDecl EnumDecl) {
        this.EnumDeclMore=EnumDeclMore;
        if(EnumDeclMore!=null) EnumDeclMore.setParent(this);
        this.EnumDecl=EnumDecl;
        if(EnumDecl!=null) EnumDecl.setParent(this);
    }

    public EnumDeclMore getEnumDeclMore() {
        return EnumDeclMore;
    }

    public void setEnumDeclMore(EnumDeclMore EnumDeclMore) {
        this.EnumDeclMore=EnumDeclMore;
    }

    public EnumDecl getEnumDecl() {
        return EnumDecl;
    }

    public void setEnumDecl(EnumDecl EnumDecl) {
        this.EnumDecl=EnumDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumDeclMore!=null) EnumDeclMore.accept(visitor);
        if(EnumDecl!=null) EnumDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumDeclMore!=null) EnumDeclMore.traverseTopDown(visitor);
        if(EnumDecl!=null) EnumDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumDeclMore!=null) EnumDeclMore.traverseBottomUp(visitor);
        if(EnumDecl!=null) EnumDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDeclMoreEnums(\n");

        if(EnumDeclMore!=null)
            buffer.append(EnumDeclMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumDecl!=null)
            buffer.append(EnumDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclMoreEnums]");
        return buffer.toString();
    }
}
