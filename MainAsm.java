import org.objectweb.asm.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainAsm {

    public static void main(String[] args) throws IOException {

        String curDir = System.getProperty("user.dir");
        String classPath = "HelloWorld.class";
        File file = new File(curDir + "\\" + classPath);
        byte[] bytes = Files.readAllBytes(file.toPath());
        
        ClassReader reader = new ClassReader(bytes);
        reader.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                System.out.println("Class: " + name);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.println("Method: " + name);
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitInsn(int opcode) {
                        System.out.println("Bytecode: " + opcodeToString(opcode));
                        translateToM68000(opcode);
                    }
                };
            }

            private String opcodeToString(int opcode) {
                switch (opcode) {
                    case Opcodes.RETURN: return "RETURN";
                    case Opcodes.GETSTATIC: return "GETSTATIC";
                    case Opcodes.LDC: return "LDC";
                    case Opcodes.INVOKEVIRTUAL: return "INVOKEVIRTUAL";
                    default: return "UNKNOWN";
                }
            }

            private void translateToM68000(int opcode) {
                switch (opcode) {
                    case Opcodes.RETURN:
                        System.out.println("M68000: RTS"); // Return from subroutine
                        break;
                    case Opcodes.GETSTATIC:
                        System.out.println("M68000: MOVE.L"); // Example translation
                        break;
                    case Opcodes.LDC:
                        System.out.println("M68000: PEA"); // Push effective address
                        break;
                    case Opcodes.INVOKEVIRTUAL:
                        System.out.println("M68000: JSR"); // Jump to subroutine
                        break;
                    default:
                        System.out.println("M68000: Unknown translation");
                        break;
                }
            }
        }, 0);
    }
}
