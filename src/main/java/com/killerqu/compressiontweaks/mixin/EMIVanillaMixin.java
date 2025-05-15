package com.killerqu.compressiontweaks.mixin;

import dev.emi.emi.EmiUtil;
import dev.emi.emi.VanillaPlugin;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin alters the hardcoded repair recipes in EMI for elytras and shields to use the tags from Property Modifier
@Mixin(VanillaPlugin.class)
public abstract class EMIVanillaMixin {

    @Shadow(remap = false)
    protected static ResourceLocation synthetic(String type, String name) {
        return null;
    }

    @Unique
    private static final TagKey<Item> ELYTRA_REPAIR_MATERIALS = ItemTags.create(new ResourceLocation("propertymodifier", "elytra_repair_material"));
    @Unique
    private static final TagKey<Item> SHIELD_REPAIR_MATERIALS = ItemTags.create(new ResourceLocation("propertymodifier", "shield_repair_material"));

    //These two target the specific suppliers to actually make the EmiAnvilRecipe. So we just cancel them with the "correct" ones.
    @Inject(method = "lambda$addRepair$42", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void changeElytraRepairMaterial(CallbackInfoReturnable<EmiRecipe> cir){
        cir.setReturnValue(new EmiAnvilRecipe(EmiStack.of(Items.ELYTRA), EmiIngredient.of(ELYTRA_REPAIR_MATERIALS),
                synthetic("anvil/repairing/material", EmiUtil.subId(Items.ELYTRA) + "/" + EmiUtil.subId(Items.PHANTOM_MEMBRANE))));
    }
    @Inject(method = "lambda$addRepair$43", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void changeShieldRepairMaterial(CallbackInfoReturnable<EmiRecipe> cir){
        cir.setReturnValue(new EmiAnvilRecipe(EmiStack.of(Items.SHIELD), EmiIngredient.of(SHIELD_REPAIR_MATERIALS),
                synthetic("anvil/repairing/material", EmiUtil.subId(Items.SHIELD) + "/" + EmiUtil.subId(Items.OAK_PLANKS))));
    }

}
