package eu.minemania.watson.render;

import com.mojang.blaze3d.systems.RenderSystem;

import eu.minemania.watson.config.Configs;
import eu.minemania.watson.data.DataManager;
import eu.minemania.watson.db.BlockEditSet;
import eu.minemania.watson.selection.EditSelection;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Fog;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profilers;
import org.joml.Matrix4fStack;

public class WatsonRenderer
{
    private static final WatsonRenderer INSTANCE = new WatsonRenderer();

    private MinecraftClient mc;

    public static WatsonRenderer getInstance()
    {
        return INSTANCE;
    }

    public void piecewiseRenderEntities(MinecraftClient mc)
    {
        if (this.mc == null)
        {
            this.mc = mc;
        }
        if (Configs.Generic.DISPLAYED.getBooleanValue() && this.mc.getCameraEntity() != null && Configs.Outlines.OUTLINE_SHOWN.getBooleanValue())
        {
            Profilers.get().push("watson_entities");
            RenderSystem.setShaderFog(Fog.DUMMY);
            EditSelection selection = DataManager.getEditSelection();
            BlockEditSet edits = selection.getBlockEditSet();
            Matrix4fStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.pushMatrix();
            RenderSystem.disableCull();

            RenderUtils.setupBlend();

            RenderUtils.color(1f, 1f, 1f, 1f);
            RenderSystem.depthMask(false);

            RenderSystem.disableDepthTest();

            Vec3d cameraPos = this.mc.gameRenderer.getCamera().getPos();

            matrixStack.translate((float) -cameraPos.getX(), (float) -cameraPos.getY(), (float) -cameraPos.getZ());
            edits.drawOutlines(matrixStack);
            edits.drawVectors();
            selection.drawSelection();

            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);

            RenderSystem.disableBlend();

            RenderSystem.enableCull();

            matrixStack.popMatrix();
            Profilers.get().pop();
        }
    }
}