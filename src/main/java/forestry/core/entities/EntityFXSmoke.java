/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.entities;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityFXSmoke extends EntityFX {
	private final float ignitionParticleScale;

	public EntityFXSmoke(World world, double x, double y, double z) {
		super(world, x, y, z, 0, 0, 0);
		this.xSpeed *= 0.8;
		this.xSpeed *= 0.8;
		this.xSpeed *= 0.8;
		this.xSpeed = this.rand.nextFloat() * 0.2F + 0.05F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleScale *= this.rand.nextFloat() / 4;
		this.ignitionParticleScale = this.particleScale;
		this.particleMaxAge = (int) (16.0 / (Math.random() * 0.8 + 0.2));
		this.setParticleTextureIndex(49);
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		int i = super.getBrightnessForRender(p_70070_1_);
		int j = i >> 16 & 255;
		return 240 | j << 16;
	}

	@Override
	public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f6 = (this.particleAge + partialTicks) / this.particleMaxAge;
		this.particleScale = this.ignitionParticleScale * (1.0F - f6 * f6);
		super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		float f = (float) this.particleAge / (float) this.particleMaxAge;

		if (this.rand.nextFloat() > f * 2) {
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY, this.posZ, this.xSpeed, this.ySpeed, this.zSpeed);
		}

		this.ySpeed -= 0.03D;
		this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
		this.xSpeed *= 0.999D;
		this.ySpeed *= 0.999D;
		this.zSpeed *= 0.999D;

		if (this.isCollided) {
			this.xSpeed *= 0.7;
			this.zSpeed *= 0.7;
		}
	}
}