package kaptainwutax.seedcrackerX.mixin;

import kaptainwutax.seedcrackerX.finder.Finder;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DimensionType.class)
public class DimensionTypeMixin implements Finder.DimensionTypeCaller {

    @Shadow
    @Final
    private TagKey<Block> infiniburn;

    @Override
    public Identifier getInfiniburn() {
        return this.infiniburn.id();
    }

}
