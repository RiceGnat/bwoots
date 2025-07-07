package moe.ricegnat.bwoots.enchantment;

import moe.ricegnat.bwoots.Bwoots;
import moe.ricegnat.bwoots.enchantment.effect.FlameParticlesEffect;
import moe.ricegnat.bwoots.tags.ModTags;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> SLIMY = of("slimy");
    public static final RegistryKey<Enchantment> UPDRAFT = of("updraft");
    public static final RegistryKey<Enchantment> METEOR = of("meteor");

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, SLIMY, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                5,
                1,
                Enchantment.leveledCost(5, 10),
                Enchantment.leveledCost(25, 15),
                8,
                AttributeModifierSlot.FEET))
                .exclusiveSet(enchantments.getOrThrow(ModTags.Enchantments.BOOTS_EXCLUSIVE_SET))
        );

        register(registerable, UPDRAFT, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                5,
                1,
                Enchantment.leveledCost(5, 10),
                Enchantment.leveledCost(25, 15),
                12,
                AttributeModifierSlot.FEET))
                .exclusiveSet(enchantments.getOrThrow(ModTags.Enchantments.BOOTS_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.ATTRIBUTES,
                        new AttributeEnchantmentEffect(
                                Identifier.of(Bwoots.MOD_ID, "safe_fall"),
                                EntityAttributes.SAFE_FALL_DISTANCE,
                                EnchantmentLevelBasedValue.constant(30),
                                EntityAttributeModifier.Operation.ADD_VALUE))
        );

        register(registerable, METEOR, Enchantment.builder(Enchantment.definition(
                items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                5,
                1,
                Enchantment.leveledCost(5, 10),
                Enchantment.leveledCost(25, 15),
                12,
                AttributeModifierSlot.FEET))
                .exclusiveSet(enchantments.getOrThrow(ModTags.Enchantments.BOOTS_EXCLUSIVE_SET))
                .addEffect(EnchantmentEffectComponentTypes.ATTRIBUTES,
                        new AttributeEnchantmentEffect(
                            Identifier.of(Bwoots.MOD_ID, "safe_fall"),
                            EntityAttributes.SAFE_FALL_DISTANCE,
                            EnchantmentLevelBasedValue.constant(20),
                            EntityAttributeModifier.Operation.ADD_VALUE))
                .addEffect(EnchantmentEffectComponentTypes.ATTRIBUTES,
                        new AttributeEnchantmentEffect(
                                Identifier.of(Bwoots.MOD_ID, "fall_damage"),
                                EntityAttributes.FALL_DAMAGE_MULTIPLIER,
                                EnchantmentLevelBasedValue.constant(-0.2f),
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE))
                .addEffect(EnchantmentEffectComponentTypes.LOCATION_CHANGED,
                        new AttributeEnchantmentEffect(
                                Identifier.of(Bwoots.MOD_ID, "gravity"),
                                EntityAttributes.GRAVITY,
                                EnchantmentLevelBasedValue.constant(1),
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EntityPropertiesLootCondition.builder(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.create()
                                        .flags(EntityFlagsPredicate.Builder.create()
                                                .sneaking(true)
                                                .onGround(false)
                                                .flying(false)
                                                .swimming(false))))
                .addEffect(EnchantmentEffectComponentTypes.LOCATION_CHANGED,
                        new FlameParticlesEffect(),
                        EntityPropertiesLootCondition.builder(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.create()
                                        .flags(EntityFlagsPredicate.Builder.create()
                                                .sneaking(true)
                                                .onGround(false)
                                                .flying(false)
                                                .swimming(false))))
        );
    }

    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Bwoots.MOD_ID, name));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
