package PatchEverything.util;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.*;

public class ExprViewer extends ExprEditor {
    private boolean firstLine = true;
    protected String tagLine = "";

    public ExprViewer(String tagLine) {super(); this.tagLine = tagLine;}
    public ExprViewer() {this("Unnamed Instrument Patch");}

    @Override public void edit(MethodCall m) throws CannotCompileException {
        if (firstLine) {System.out.println(); System.out.println(tagLine); firstLine = false;}

        if (m.getClassName() != null)
            System.out.print("Class " + m.getClassName() + " ");
        if (m.getMethodName() != null)
            System.out.print("Method: " + m.getMethodName());
        System.out.println();
    }

    @Override
    public void edit(Cast c) throws CannotCompileException {
        if (firstLine) {System.out.println(); System.out.println(tagLine); firstLine = false;}

        try {
            System.out.println("Cast: " + c.getType().getSimpleName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void edit(FieldAccess f) throws CannotCompileException {
        if (firstLine) {System.out.println(); System.out.println(tagLine); firstLine = false;}

        System.out.println("Type: " + f.getClassName() + " Field: " + f.getFieldName());
    }

    @Override
    public void edit(Instanceof i) throws CannotCompileException {
        if (firstLine) {System.out.println(); System.out.println(tagLine); firstLine = false;}

        try {
            System.out.println("Instance of: " + i.getType().getSimpleName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void edit(ConstructorCall c) throws CannotCompileException {
        if (firstLine) {System.out.println(); System.out.println(tagLine); firstLine = false;}

        System.out.println("Constructor: " + c.getMethodName());
    }
}
