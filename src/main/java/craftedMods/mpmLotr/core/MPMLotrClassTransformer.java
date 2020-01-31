/*******************************************************************************
 * Copyright (C) 2020 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.mpmLotr.core;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class MPMLotrClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.equals("noppes.mpm.client.EntityRendererAlt")) {
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(basicClass);
			classReader.accept(classNode, 0);

			classNode.superName = "lotr/client/LOTREntityRenderer";
			for (MethodNode node : classNode.methods) {
				for (int i = 0; i < node.instructions.size(); i++) {
					AbstractInsnNode insn = node.instructions.get(i);
					if (insn.getOpcode() == Opcodes.INVOKESPECIAL) {
						MethodInsnNode methodNode = (MethodInsnNode) insn;
						if (methodNode.owner.equals("net/minecraft/client/renderer/EntityRenderer")
								|| methodNode.owner.equals("blt")) {
							methodNode.owner = "lotr/client/LOTREntityRenderer";
						}
					}
				}
			}

			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			classNode.accept(writer);

			System.out.println("Patched \"noppes.mpm.client.EntityRendererAlt\" to work with the LOTR Mod");
			return writer.toByteArray();
		} else {
			return basicClass;
		}
	}

}
