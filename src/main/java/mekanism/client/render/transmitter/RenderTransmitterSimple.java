package mekanism.client.render.transmitter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javax.annotation.ParametersAreNonnullByDefault;
import mekanism.client.render.MekanismRenderType;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.GlowInfo;
import mekanism.common.tile.transmitter.TileEntityTransmitter;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.profiler.IProfiler;

@ParametersAreNonnullByDefault
public abstract class RenderTransmitterSimple<T extends TileEntityTransmitter<?, ?, ?>> extends RenderTransmitterBase<T> {

    public RenderTransmitterSimple(TileEntityRendererDispatcher renderer) {
        super(renderer);
    }

    protected abstract void renderContents(MatrixStack matrix, IVertexBuilder renderer, T transmitter, int light, int overlayLight);

    protected void render(T transmitter, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight, int glow, IProfiler profiler) {
        matrix.push();
        IVertexBuilder buffer = renderer.getBuffer(MekanismRenderType.transmitterContents(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
        matrix.translate(0.5, 0.5, 0.5);
        GlowInfo glowInfo = MekanismRenderer.enableGlow(glow);
        renderContents(matrix, buffer, transmitter, light, overlayLight);
        MekanismRenderer.disableGlow(glowInfo);
        matrix.pop();
    }
}