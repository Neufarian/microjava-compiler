// generated with ast extension for cup
// version 0.8
// 9/2/2026 13:36:55


package rs.ac.bg.etf.pp1.ast;

public class FormParsListSingle extends FormParsList {

    private FormParDecl FormParDecl;

    public FormParsListSingle (FormParDecl FormParDecl) {
        this.FormParDecl=FormParDecl;
        if(FormParDecl!=null) FormParDecl.setParent(this);
    }

    public FormParDecl getFormParDecl() {
        return FormParDecl;
    }

    public void setFormParDecl(FormParDecl FormParDecl) {
        this.FormParDecl=FormParDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParDecl!=null) FormParDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParDecl!=null) FormParDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParDecl!=null) FormParDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsListSingle(\n");

        if(FormParDecl!=null)
            buffer.append(FormParDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsListSingle]");
        return buffer.toString();
    }
}
