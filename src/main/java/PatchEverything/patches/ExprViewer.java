package PatchEverything.patches;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.*;

public class ExprViewer extends ExprEditor {
    @Override public void edit(MethodCall m) throws CannotCompileException {
        if (m.getClassName() != null)
            System.out.print("Class " + m.getClassName());
        if (m.getMethodName() != null)
            System.out.print("Method: " + m.getMethodName());
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
        System.out.println("Type: " + f.getClassName() + " Field: " + f.getFieldName());
    }

    @Override
    public void edit(Instanceof i) throws CannotCompileException {
        try {
            System.out.println("Instance of: " + i.getType().getSimpleName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
