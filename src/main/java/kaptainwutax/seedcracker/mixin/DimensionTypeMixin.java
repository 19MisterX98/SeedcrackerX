package kaptainwutax.seedcracker.mixin;

import kaptainwutax.seedcracker.finder.Finder;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements Finder.DimensionTypeCaller {

	@Shadow @Final private Identifier infiniburn;

	@Override
	public Identifier getInfiniburn() {
		return this.infiniburn;
	}

}
