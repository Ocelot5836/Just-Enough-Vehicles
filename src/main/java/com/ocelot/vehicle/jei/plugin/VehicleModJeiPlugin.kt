package com.ocelot.vehicle.jei.plugin

import com.mrcrayfish.vehicle.client.gui.GuiFluidMixer
import com.mrcrayfish.vehicle.common.container.ContainerFluidMixer
import com.mrcrayfish.vehicle.init.ModBlocks
import com.mrcrayfish.vehicle.init.ModItems
import com.mrcrayfish.vehicle.item.ItemPart
import com.ocelot.vehicle.jei.VehicleModJei
import com.ocelot.vehicle.jei.plugin.fluidmixer.FluidMixerCategory
import com.ocelot.vehicle.jei.plugin.fluidmixer.FluidMixerRecipeMaker
import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.ForgeRegistries

@JEIPlugin
class VehicleModJeiPlugin : IModPlugin {

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        val guiHelper = registry.jeiHelpers.guiHelper
        registry.addRecipeCategories(FluidMixerCategory(guiHelper))
    }

    override fun register(registry: IModRegistry) {
        val ingredientRegistry = registry.ingredientRegistry
        val jeiHelpers = registry.jeiHelpers

        registry.addRecipes(FluidMixerRecipeMaker.getRecipes(jeiHelpers), VehicleModJei.FLUID_MIXER_UID)

        registry.addRecipeClickArea(GuiFluidMixer::class.java, 151, 20, 16, 59, VehicleModJei.FLUID_MIXER_UID, VanillaRecipeCategoryUid.FUEL)

        registry.addRecipeCatalyst(ItemStack(ModBlocks.FLUID_MIXER), VanillaRecipeCategoryUid.FUEL)
        registry.addRecipeCatalyst(ItemStack(ModBlocks.FLUID_MIXER), VehicleModJei.FLUID_MIXER_UID)

        val recipeTransferRegistry = registry.recipeTransferRegistry
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidMixer::class.java, VehicleModJei.FLUID_MIXER_UID, 1, 1, 2, 36)

        val ingredientBlacklist = registry.jeiHelpers.ingredientBlacklist

        // Blacklist the models for vehicles
        ingredientBlacklist.addIngredientToBlacklist(ItemStack(ModItems.MODELS))
        for (item in ForgeRegistries.ITEMS) {
            if (item is ItemPart) {
                ingredientBlacklist.addIngredientToBlacklist(ItemStack(item))
            }
        }
    }

    // Fuel Mixer Notes
    // First slot is any furnace fuel type
    // Two Tanks are the inputs
    // Middle slot is specified in the recipe
    // Last Tank is the output
}