package moe.ricegnat.bwoots.tags;

import moe.ricegnat.bwoots.Bwoots;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Enchantments {
        public static final TagKey<Enchantment> BOOTS_EXCLUSIVE_SET = createTag("exclusive_set/feet_fallin");

        private static TagKey<Enchantment> createTag(String name) {
            return TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Bwoots.MOD_ID, name));
        }
    }
}
