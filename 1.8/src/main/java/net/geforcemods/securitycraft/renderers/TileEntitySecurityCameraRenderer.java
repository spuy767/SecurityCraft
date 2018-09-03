package net.geforcemods.securitycraft.renderers;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.blocks.BlockSecurityCamera;
import net.geforcemods.securitycraft.models.ModelSecurityCamera;
import net.geforcemods.securitycraft.tileentity.TileEntitySecurityCamera;
import net.geforcemods.securitycraft.util.BlockUtils;
import net.geforcemods.securitycraft.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntitySecurityCameraRenderer extends TileEntitySpecialRenderer {

	private static final ModelSecurityCamera model = new ModelSecurityCamera();
	private static final ResourceLocation TEXTURE = new ResourceLocation("securitycraft:textures/blocks/securityCamera1.png");


	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double x, double y, double z, float par5, int par6) {
		if(((TileEntitySecurityCamera)par1TileEntity).down || PlayerUtils.isPlayerMountedOnCamera(Minecraft.getMinecraft().thePlayer) && Minecraft.getMinecraft().thePlayer.ridingEntity.getPosition().equals(par1TileEntity.getPos()))
			return;

		float rotation = 0F;

		if(par1TileEntity.hasWorldObj()){
			Tessellator tessellator = Tessellator.getInstance();
			float f = par1TileEntity.getWorld().getLightBrightness(par1TileEntity.getPos());
			int l = par1TileEntity.getWorld().getCombinedLight(par1TileEntity.getPos(), 0);
			int l1 = l % 65536;
			int l2 = l / 65536;
			tessellator.getWorldRenderer().setColorOpaque_F(f, f, f);

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);

		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

		GlStateManager.pushMatrix();

		if(par1TileEntity.hasWorldObj() && BlockUtils.getBlock(par1TileEntity.getWorld(), par1TileEntity.getPos()) == SCContent.securityCamera){
			EnumFacing side = BlockUtils.getBlockPropertyAsEnum(getWorld(), par1TileEntity.getPos(), BlockSecurityCamera.FACING);

			if(side == EnumFacing.EAST)
				rotation = -1F;
			else if(side == EnumFacing.SOUTH)
				rotation = -10000F;
			else if(side == EnumFacing.WEST)
				rotation = 1F;
			else if(side == EnumFacing.NORTH)
				rotation = 0F;
		}
		else
			rotation = -10000F;

		GlStateManager.rotate(180F, rotation, 0.0F, 1.0F);

		modelSecurityCamera.cameraRotationPoint.rotateAngleY = ((TileEntitySecurityCamera) par1TileEntity).cameraRotation;

		modelSecurityCamera.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
