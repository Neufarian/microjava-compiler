// generated with ast extension for cup
// version 0.8
// 9/2/2026 13:36:55


package rs.ac.bg.etf.pp1.ast;

public class DesignatorEnum extends Designator {

    private ArrayName ArrayName;
    private String I2;

    public DesignatorEnum (ArrayName ArrayName, String I2) {
        this.ArrayName=ArrayName;
        if(ArrayName!=null) ArrayName.setParent(this);
        this.I2=I2;
    }

    public ArrayName getArrayName() {
        return ArrayName;
    }

    public void setArrayName(ArrayName ArrayName) {
        this.ArrayName=ArrayName;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrayName!=null) ArrayName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrayName!=null) ArrayName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrayName!=null) ArrayName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorEnum(\n");

        if(ArrayName!=null)
            buffer.append(ArrayName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorEnum]");
        return buffer.toString();
    }
}
