package moe.ricegnat.bwoots;

import moe.ricegnat.bwoots.datagen.ModEnchantmentTagProvider;
import moe.ricegnat.bwoots.datagen.ModRegistryDataGenerator;
import moe.ricegnat.bwoots.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class BwootsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModEnchantmentTagProvider::new);
		pack.addProvider(ModRegistryDataGenerator::new);
;	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
	}

}
