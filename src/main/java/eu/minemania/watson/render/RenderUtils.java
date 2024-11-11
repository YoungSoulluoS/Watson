package eu.minemania.watson.render;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.PositionUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

public class RenderUtils
{
    private static final Random RAND = Random.create();

    public static BufferBuilder startDrawingLines(Tessellator tessellator)
    {
        RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
        return tessellator.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
    }

    //START TEMP MALILIB
    /**
     * Assumes a BufferBuilder in GL_LINES mode has been initialized
     */
    public static void drawBlockBoundingBoxOutlinesBatchedLines(BlockPos pos, Color4f color, double expand, BufferBuilder buffer)
    {
        drawBlockBoundingBoxOutlinesBatchedLines(pos, Vec3d.ZERO, color, expand, buffer);
    }

    /**
     * Assumes a BufferBuilder in GL_LINES mode has been initialized.
     * The cameraPos value will be subtracted from the absolute coordinate values of the passed in BlockPos.
     */
    public static void drawBlockBoundingBoxOutlinesBatchedLines(BlockPos pos, Vec3d cameraPos, Color4f color, double expand, BufferBuilder buffer)
    {
        double minX = pos.getX() - expand - cameraPos.x;
        double minY = pos.getY() - expand - cameraPos.y;
        double minZ = pos.getZ() - expand - cameraPos.z;
        double maxX = pos.getX() + expand - cameraPos.x + 1;
        double maxY = pos.getY() + expand - cameraPos.y + 1;
        double maxZ = pos.getZ() + expand - cameraPos.z + 1;

        drawBoxAllEdgesBatchedLines(minX, minY, minZ, maxX, maxY, maxZ, color, buffer);
    }

    /**
     * Assumes a BufferBuilder in GL_LINES mode has been initialized
     */
    public static void drawBoxAllEdgesBatchedLines(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                                                   Color4f color, BufferBuilder buffer)
    {
        // West side
        buffer.vertex((float) minX, (float) minY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) minX, (float) minY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) minX, (float) minY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) minX, (float) maxY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) minX, (float) maxY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) minX, (float) maxY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) minX, (float) maxY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) minX, (float) minY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        // East side
        buffer.vertex((float) maxX, (float) minY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) maxX, (float) minY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) maxX, (float) minY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) maxX, (float) maxY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) maxX, (float) maxY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) maxX, (float) maxY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) maxX, (float) maxY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) maxX, (float) minY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        // North side (don't repeat the vertical lines that are done by the east/west sides)
        buffer.vertex((float) maxX, (float) minY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) minX, (float) minY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) minX, (float) maxY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) maxX, (float) maxY, (float) minZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        // South side (don't repeat the vertical lines that are done by the east/west sides)
        buffer.vertex((float) minX, (float) minY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) maxX, (float) minY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex((float) maxX, (float) maxY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex((float) minX, (float) maxY, (float) maxZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
    }
    //END TEMP MALILIB

    public static void drawFullBlockOutlinesBatched(float x, float y, float z, Color4f color, BufferBuilder buffer)
    {
        buffer.vertex(x, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + 1F, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y + 1F, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y + 1F, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y + 1F, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + 1F, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y + 1F, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y + 1F, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + 1F, y + 1F, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y + 1F, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + 1F, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + 1F, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y + 1F, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + 1F, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + 1F, y, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + 1F, y + 1, z + 1F).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
    }

    public static void drawSpecialOutlinesBatched(float x, float y, float z, Color4f color, BufferBuilder buffer, boolean sign)
    {
        float posX = x + 0.25F / 2;
        float posY = y + 0.25F / 2;
        float posZ = z + 0.015F;
        float widthX = (12 / 32.0F) * 2;
        float heightY = (12 / 32.0F) * 2;
        float widthZ = (1.0F / 32.0F) * 2;

        if (sign)
        {
            posX = posX - 0.1F;
            posY = posY + 0.2F;
            widthX = widthX + 0.2F;
            heightY = heightY - 0.3F;
        }

        buffer.vertex(posX, posY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX, posY + heightY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY + heightY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX, posY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX, posY + heightY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX + widthX, posY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY + heightY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX, posY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX, posY + heightY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY + heightY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX, posY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX, posY + heightY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX + widthX, posY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY + heightY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX + widthX, posY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX, posY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX, posY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX + widthX, posY + heightY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX + widthX, posY + heightY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(posX, posY + heightY, posZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(posX, posY + heightY, posZ + widthZ).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
    }

    public static void drawBedOutlineBatched(float x, float y, float z, Color4f color, BufferBuilder buffer)
    {
        float shortLength = 0.19f;
        float reverseShortLength = 0.81f;
        float otherSide = 1f;
        float longHeight = 0.56f;

        //left front leg
        buffer.vertex(x, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + shortLength, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + longHeight, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + shortLength, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + shortLength, y + shortLength, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + shortLength, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        //right front leg
        buffer.vertex(x + reverseShortLength, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + longHeight, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + reverseShortLength, y, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + reverseShortLength, y + shortLength, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + shortLength, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        //left back leg
        buffer.vertex(x, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + shortLength, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + longHeight, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + shortLength, y, z + 1).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + shortLength, y + shortLength, z + 1).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + shortLength, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        //right back leg
        buffer.vertex(x + reverseShortLength, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + longHeight, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + reverseShortLength, y, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + reverseShortLength, y + shortLength, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + shortLength, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        //middle connections
        buffer.vertex(x + shortLength, y + shortLength, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + reverseShortLength, y + shortLength, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y + shortLength, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + shortLength, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + shortLength, y + shortLength, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + reverseShortLength, y + shortLength, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y + shortLength, z + shortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + shortLength, z + reverseShortLength).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        //top connections
        buffer.vertex(x, y + longHeight, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + longHeight, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y + longHeight, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x, y + longHeight, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x, y + longHeight, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + longHeight, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);

        buffer.vertex(x + otherSide, y + longHeight, z).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
        buffer.vertex(x + otherSide, y + longHeight, z + otherSide).color(color.r, color.g, color.b, color.a).normal(0, 0, 0);
    }

    /**
     * Assumes a BufferBuilder in the GL_LINES mode has been initialized
     */
    public static void drawBlockModelOutlinesBatched(BakedModel model, BlockState state, BlockPos pos, Color4f color, BufferBuilder buffer)
    {
        for (final Direction side : PositionUtils.ALL_DIRECTIONS)
        {
            renderModelQuadOutlines(pos, color, model.getQuads(state, side, RAND), buffer);
        }

        renderModelQuadOutlines(pos, color, model.getQuads(state, null, RAND), buffer);
    }

    private static void renderModelQuadOutlines(BlockPos pos, Color4f color, List<BakedQuad> quads, BufferBuilder buffer)
    {
        for (BakedQuad quad : quads)
        {
            renderQuadOutlinesBatched(pos, color, quad, buffer);
        }
    }

    private static void renderQuadOutlinesBatched(BlockPos pos, Color4f color, BakedQuad quad, BufferBuilder buffer)
    {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();
        int[] vertexData = quad.getVertexData();
        Vec3i vec3i = quad.getFace().getVector();
        Vector3f vec3f = new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        final int vertexSize = vertexData.length / 4;
        float[] fx = new float[4];
        float[] fy = new float[4];
        float[] fz = new float[4];

        for (int index = 0; index < 4; ++index)
        {
            fx[index] = x + Float.intBitsToFloat(vertexData[index * vertexSize]);
            fy[index] = y + Float.intBitsToFloat(vertexData[index * vertexSize + 1]);
            fz[index] = z + Float.intBitsToFloat(vertexData[index * vertexSize + 2]);
        }

        buffer.vertex(fx[0], fy[0], fz[0]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);
        buffer.vertex(fx[1], fy[1], fz[1]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);

        buffer.vertex(fx[1], fy[1], fz[1]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);
        buffer.vertex(fx[2], fy[2], fz[2]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);

        buffer.vertex(fx[2], fy[2], fz[2]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);
        buffer.vertex(fx[3], fy[3], fz[3]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);

        buffer.vertex(fx[3], fy[3], fz[3]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);
        buffer.vertex(fx[0], fy[0], fz[0]).color(color.r, color.g, color.b, color.a).normal(vec3f.x, vec3f.y, vec3f.z);
    }
}