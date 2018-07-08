package net.geforcemods.securitycraft.renderers;

import org.lwjgl.opengl.GL11;

import net.geforcemods.securitycraft.models.ModelMotionSensoredLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityMotionLightRenderer extends TileEntitySpecialRenderer {
	
	private ModelMotionSensoredLight lightModel;
	private ResourceLocation texture = new ResourceLocation("securitycraft:textures/blocks/motion_activated_light.png");

	public TileEntityMotionLightRenderer(){
		lightModel = new ModelMotionSensoredLight();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
		int meta = te.hasWorldObj() ? te.getBlockMetadata() : te.blockMetadata;
		float rotation = 0F;

		if(te.hasWorldObj()){
			Tessellator tessellator = Tessellator.instance;
			float brightness = te.getWorld().getLightBrightness(te.xCoord, te.yCoord, te.zCoord);
			int skyBrightness = te.getWorld().getLightBrightnessForSkyBlocks(te.xCoord, te.yCoord, te.zCoord, 0);
			int l1 = skyBrightness % 65536;
			int l2 = skyBrightness / 65536;
			tessellator.setColorOpaque_F(brightness, brightness, brightness);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, l1, l2);
		}

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glPushMatrix();

		if(te.hasWorldObj())
		{
			if(meta == 2)
				rotation = 1F;
			else if(meta == 3)
				rotation = -10000F;
			else if(meta == 4)
				rotation = 0F;
			else if(meta == 1)
				rotation = -1F;
		}
		else
		{
			rotation = -1F;
			GL11.glScalef(2F, 2F, 2F);
			GL11.glTranslatef(0.35F, 0.6F, 0F);
		}

		GL11.glRotatef(180F, rotation, 0.0F, 1.0F);

		lightModel.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}