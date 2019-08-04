package io.github.vampirestudios.hgm.utils;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IItemColorProvider {

	@OnlyIn(Dist.CLIENT)
	public IItemColor getItemColor();

}
