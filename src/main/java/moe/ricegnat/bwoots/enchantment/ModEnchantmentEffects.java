package moe.ricegnat.bwoots.enchantment;

import com.mojang.serialization.MapCodec;
import moe.ricegnat.bwoots.Bwoots;
import moe.ricegnat.bwoots.enchantment.effect.FlameParticlesEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    private static MapCodec<? extends EnchantmentLocationBasedEffect> registerLocationBasedEffect(String name, MapCodec<? extends EnchantmentLocationBasedEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, Identifier.of(Bwoots.MOD_ID, name), codec);
    }

    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(Bwoots.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        registerLocationBasedEffect("flame_particles", FlameParticlesEffect.CODEC);
    }
}
