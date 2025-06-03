package com.killerqu.compressiontweaks.jei;

import com.killerqu.compressiontweaks.CompressionTweaks;
import com.killerqu.compressiontweaks.recipe.BoulderInfoRecipe;
import com.killerqu.compressiontweaks.recipe.CTRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid(){
        return new ResourceLocation(CompressionTweaks.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        registration.addRecipeCategories(new BoulderInfoCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration){
        Level mcLevel = Minecraft.getInstance().level;
        if(mcLevel == null) return;
        List<BoulderInfoRecipe> boulders = mcLevel.getRecipeManager().getAllRecipesFor(CTRecipeTypes.BOULDER_INFO.get());
        registration.addRecipes(BoulderInfoCategory.TYPE, boulders);

    }
}
