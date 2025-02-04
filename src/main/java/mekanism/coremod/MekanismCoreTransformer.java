package mekanism.coremod;


import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static mekanism.coremod.MekanismCoremod.mainLogger;
import static mekanism.coremod.MekanismCoremod.runtimeDeobfEnabled;
import static org.objectweb.asm.Opcodes.*;

public class MekanismCoreTransformer implements IClassTransformer {

    protected static class ObfSafeName {
        final String deobf, srg;

        public ObfSafeName(String deobf, String srg) {
            this.deobf = deobf;
            this.srg = srg;
        }

        public String getName() {
            return runtimeDeobfEnabled ? srg : deobf;
        }

        public boolean equals(String obj) {
            if (obj != null) {
                return obj.equals(deobf) || obj.equals(srg);
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof String) {
                return obj.equals(deobf) || obj.equals(srg);
            } else if (obj instanceof ObfSafeName obf) {
                return obf.deobf.equals(deobf) && obf.srg.equals(srg);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

    }


    protected static abstract class Transform {
        abstract void transform(Iterator<MethodNode> methods);
    }

    static final String renderItemClass = "net.minecraft.client.renderer.RenderItem";
    static final ObfSafeName renderItemOverlayIntoGUIMethod = new ObfSafeName("renderItemOverlayIntoGUI", "func_180453_a");
    static final ObfSafeName renderItemAndEffectIntoGUI = new ObfSafeName("renderItemAndEffectIntoGUI", "func_184391_a");
    static final ObfSafeName renderItemDisplayName = new ObfSafeName("renderItemAndEffectIntoGUI, renderItemOverlayIntoGUI", "func_180453_a, func_184391_a");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {


        // Item Overlay Rendering hook
        if (transformedName.equals(renderItemClass)) {
            return transform(basicClass, renderItemClass, renderItemDisplayName, new Transform() {
                @Override
                void transform(Iterator<MethodNode> methods) {
                    int done = 0;
                    while (methods.hasNext()) {
                        MethodNode m = methods.next();
                        if (renderItemAndEffectIntoGUI.equals(m.name) && "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;II)V".equals(m.desc)) {
                            InsnList toAdd = new InsnList();
                            toAdd.add(new VarInsnNode(ALOAD, 2));
                            toAdd.add(new VarInsnNode(ILOAD, 3));
                            toAdd.add(new VarInsnNode(ILOAD, 4));
                            toAdd.add(new MethodInsnNode(INVOKESTATIC, "mekanism/coremod/MekanismCoreMethods", "renderItemAndEffectIntoGUI",
                                    "(Lnet/minecraft/item/ItemStack;II)V", false));
                            m.instructions.insert(toAdd);
                            done++;
                        }
                        if (renderItemOverlayIntoGUIMethod.equals(m.name)) {

                            InsnList toAdd = new InsnList();
                            toAdd.add(new VarInsnNode(ALOAD, 2));
                            toAdd.add(new VarInsnNode(ILOAD, 3));
                            toAdd.add(new VarInsnNode(ILOAD, 4));
                            toAdd.add(new MethodInsnNode(INVOKESTATIC, "mekanism/coremod/MekanismCoreMethods", "renderItemOverlayIntoGUI",
                                    "(Lnet/minecraft/item/ItemStack;II)V", false));

                            boolean primed = false, onframe = false, applied = false;
                            Label target = null;
                            for (int i = 0; i < m.instructions.size(); i++) {
                                AbstractInsnNode next = m.instructions.get(i);

                                // (1) find "if (stack.getItem().showDurabilityBar(stack)) {"
                                if (!primed && target == null && next.getOpcode() == INVOKEVIRTUAL && next instanceof MethodInsnNode) {
                                    if ("showDurabilityBar".equals(((MethodInsnNode) next).name)) { // Forge method, never obf'ed
                                        primed = true;
                                    }
                                }

                                // (2) where is the matching "}"?
                                if (primed && next.getOpcode() == IFEQ && next instanceof JumpInsnNode) {
                                    target = ((JumpInsnNode) next).label.getLabel();
                                    primed = false;
                                }

                                // (3) insert our callback there
                                if (target != null && next instanceof LabelNode && ((LabelNode) next).getLabel() == target) {
                                    onframe = true;
                                    continue;
                                }
                                if (onframe && next instanceof FrameNode) {
                                    m.instructions.insert(next, toAdd);
                                    done++;
                                    applied = true;
                                    break;
                                }
                            }
                            if (!applied) {
                                mainLogger.info("Transforming failed. Applying ersatz patch...");
                                m.instructions.insert(toAdd);
                                mainLogger.warn("Ersatz patch applied, things may break!");
                                done++;
                            }
                            break;
                        }
                    }
                    if (done != 2) {
                        mainLogger.info("Transforming failed.");
                    }
                }
            });
        }
        return basicClass;

    }


    protected final static byte[] transform(byte[] classBytes, String className, ObfSafeName methodName, Transform transformer) {
        mainLogger.info("Transforming Class [" + className + "], Method [" + methodName.getName() + "]");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(classBytes);
        classReader.accept(classNode, 0);

        Iterator<MethodNode> methods = classNode.methods.iterator();

        transformer.transform(methods);

        ClassWriter cw = new ClassWriter(0);
        classNode.accept(cw);
        mainLogger.info("Transforming " + className + " Finished.");
        return cw.toByteArray();
    }

}
