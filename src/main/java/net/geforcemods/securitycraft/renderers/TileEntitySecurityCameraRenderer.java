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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntitySecurityCameraRenderer extends TileEntitySpecialRenderer<TileEntitySecurityCamera> {

	private static final ModelSecurityCamera modelSecurityCamera = new ModelSecurityCamera();
	private static final ResourceLocation cameraTexture = new ResourceLocation("securitycraft:textures/blocks/security_camera1.png");

	@Override
	public void render(TileEntitySecurityCamera te, double x, double y, double z, float par5, int par6, float alpha) {
		if(te.down || (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && PlayerUtils.isPlayerMountedOnCamera(Minecraft.getMinecraft().player) && Minecraft.getMinecraft().player.getRidingEntity().getPosition().equals(te.getPos())))
			return;

		float rotation = 0F;

		if(te.hasWorld()){
			Tessellator tessellator = Tessellator.getInstance();
			float brightness = te.getWorld().getLightBrightness(te.getPos());
			int skyBrightness = te.getWorld().getCombinedLight(te.getPos(), 0);
			int lightmapX = skyBrightness % 65536;
			int lightmapY = skyBrightness / 65536;
			tessellator.getBuffer().putColorRGBA(0, (int)(brightness * 255.0F), (int)(brightness * 255.0F), (int)(brightness * 255.0F), 255);

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);

		Minecraft.getMinecraft().renderEngine.bindTexture(cameraTexture);

		GlStateManager.pushMatrix();

		if(te.hasWorld() && BlockUtils.getBlock(te.getWorld(), te.getPos()) == SCContent.securityCamera){
			EnumFacing side = BlockUtils.getBlockProperty(getWorld(), te.getPos(), BlockSecurityCamera.FACING);

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

		modelSecurityCamera.cameraRotationPoint.rotateAngleY = (float)te.cameraRotation;

		modelSecurityCamera.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}


}
