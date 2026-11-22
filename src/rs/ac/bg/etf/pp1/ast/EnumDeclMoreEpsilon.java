// generated with ast extension for cup
// version 0.8
// 9/2/2026 13:36:55


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclMoreEpsilon extends EnumDeclMore {

    public EnumDeclMoreEpsilon () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDeclMoreEpsilon(\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclMoreEpsilon]");
        return buffer.toString();
    }
}
