package PatchEverything.patches;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class ExprViewer extends ExprEditor {
    @Override public void edit(MethodCall m) throws CannotCompileException {
        if (m.getMethodName() != null)
            System.out.print("Method: " + m.getMethodName());
        if (m.getClassName() != null)
            System.out.print("Class " + m.getClassName());
        System.out.println();
    }

    @Override
    public void edit(Cast c) throws CannotCompileException {
        try {
            System.out.println("Cast: " + c.getType().getSimpleName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void edit(FieldAccess f) throws CannotCompileException {
        System.out.println("Field: " + f.getFieldName() + " type " + f.getClassName());
    }
}
