package io.github.noeppi_noeppi.misc.excompressum1710fixer;

import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;

public class Main {
    
    public static void main(String[] args) throws IOException {
        FileSystem fs = FileSystems.newFileSystem(Paths.get("excompressum-mc1.7.10-1.1.149.jar"), (ClassLoader) null);
        InputStream in = Files.newInputStream(fs.getPath("net/blay09/mods/excompressum/registry/AutoSieveSkinRegistry.class"));
        ClassReader cls = new ClassReader(in);
        in.close();
        ClassWriter writer = new ClassWriter(Opcodes.ASM6);
        ClassVisitor modifier = new ClassVisitor(Opcodes.ASM6, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM6, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitLdcInsn(Object value) {
                        if (value instanceof String && value.equals("http://balyware.com/control-panel/api/skins.php")) {
                            super.visitLdcInsn("https://invalid.invalid/");
                        } else {
                            super.visitLdcInsn(value);
                        }
                    }
                };
            }
        };
        cls.accept(modifier, 0);
        byte[] bytes = writer.toByteArray();
        OutputStream out = Files.newOutputStream(fs.getPath("net/blay09/mods/excompressum/registry/AutoSieveSkinRegistry.class"), StandardOpenOption.TRUNCATE_EXISTING);
        out.write(bytes);
        out.close();
        fs.close();
    }
}
