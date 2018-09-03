package net.geforcemods.securitycraft.renderers;

import net.geforcemods.securitycraft.entity.EntityIMSBomb;
import net.geforcemods.securitycraft.models.ModelIMSBomb;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderIMSBomb extends Render<EntityIMSBomb> {

	private static final ResourceLocation imsTexture = new ResourceLocation("securitycraft:textures/entity/imsBomb.png");

	/** instance of ModelIMSBomb for rendering */
	protected static final ModelIMSBomb modelBomb = new ModelIMSBomb();

	public RenderIMSBomb(RenderManager renderManager){
		super(renderManager);
	}

	@Override
	public void doRender(EntityIMSBomb par1EntityIMSBomb, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
		GlStateManager.pushMatrix();

		GlStateManager.translate((float)p_76986_2_ - 0.1F, (float)p_76986_4_, (float)p_76986_6_ - 0.1F);
		bindEntityTexture(par1EntityIMSBomb);
		GlStateManager.scale(1.4F, 1.4F, 1.4F);
		modelBomb.render(par1EntityIMSBomb, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMSBomb p_110775_1_) {
		return imsTexture;
	}

}
