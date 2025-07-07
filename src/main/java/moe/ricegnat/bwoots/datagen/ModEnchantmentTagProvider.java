package moe.ricegnat.bwoots.datagen;

import moe.ricegnat.bwoots.enchantment.ModEnchantments;
import moe.ricegnat.bwoots.tags.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagProvider<Enchantment> {
    public ModEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getTagBuilder(ModTags.Enchantments.BOOTS_EXCLUSIVE_SET)
                .add(ModEnchantments.SLIMY.getValue())
                .add(ModEnchantments.METEOR.getValue())
                .add(Enchantments.FEATHER_FALLING.getValue());
    }
}
