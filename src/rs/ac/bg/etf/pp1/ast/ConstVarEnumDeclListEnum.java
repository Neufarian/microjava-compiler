// generated with ast extension for cup
// version 0.8
// 9/2/2026 13:36:55


package rs.ac.bg.etf.pp1.ast;

public class ConstVarEnumDeclListEnum extends ConstVarEnumDeclList {

    private ConstVarEnumDeclList ConstVarEnumDeclList;
    private EnumDeclList EnumDeclList;

    public ConstVarEnumDeclListEnum (ConstVarEnumDeclList ConstVarEnumDeclList, EnumDeclList EnumDeclList) {
        this.ConstVarEnumDeclList=ConstVarEnumDeclList;
        if(ConstVarEnumDeclList!=null) ConstVarEnumDeclList.setParent(this);
        this.EnumDeclList=EnumDeclList;
        if(EnumDeclList!=null) EnumDeclList.setParent(this);
    }

    public ConstVarEnumDeclList getConstVarEnumDeclList() {
        return ConstVarEnumDeclList;
    }

    public void setConstVarEnumDeclList(ConstVarEnumDeclList ConstVarEnumDeclList) {
        this.ConstVarEnumDeclList=ConstVarEnumDeclList;
    }

    public EnumDeclList getEnumDeclList() {
        return EnumDeclList;
    }

    public void setEnumDeclList(EnumDeclList EnumDeclList) {
        this.EnumDeclList=EnumDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstVarEnumDeclList!=null) ConstVarEnumDeclList.accept(visitor);
        if(EnumDeclList!=null) EnumDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstVarEnumDeclList!=null) ConstVarEnumDeclList.traverseTopDown(visitor);
        if(EnumDeclList!=null) EnumDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstVarEnumDeclList!=null) ConstVarEnumDeclList.traverseBottomUp(visitor);
        if(EnumDeclList!=null) EnumDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstVarEnumDeclListEnum(\n");

        if(ConstVarEnumDeclList!=null)
            buffer.append(ConstVarEnumDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumDeclList!=null)
            buffer.append(EnumDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstVarEnumDeclListEnum]");
        return buffer.toString();
    }
}
