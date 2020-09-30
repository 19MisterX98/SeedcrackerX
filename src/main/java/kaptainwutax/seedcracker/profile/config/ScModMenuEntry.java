package kaptainwutax.seedcracker.profile.config;


import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public class ScModMenuEntry implements ModMenuApi{
    ConfigScreen configscreen = new ConfigScreen();
    
    @Override
    public String getModId() {
        return "seedcracker";
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return configscreen::getConfigScreenByCloth;
    }
}
