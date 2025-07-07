package moe.ricegnat.bwoots.mixin;

import moe.ricegnat.bwoots.Bwoots;
import moe.ricegnat.bwoots.config.BwootsConfig;
import moe.ricegnat.bwoots.enchantment.ModEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Block.class)
public class BlockMixin {
    // slimy code taken from Amethyst Imbuement (https://github.com/fzzyhmstrs/ai)

    @Inject(method = "onLandedUpon", at = @At(value = "HEAD"), cancellable = true)
    private void bwoots_onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo ci){
        if (!state.isOf(Blocks.SLIME_BLOCK)) {
            if ((entity instanceof PlayerEntity playerEntity)) {
                if (!playerEntity.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {
                    var slimy = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(ModEnchantments.SLIMY);
                    if (EnchantmentHelper.getLevel(slimy, playerEntity.getEquippedStack(EquipmentSlot.FEET)) > 0) {
                        Block slimeBlock = Blocks.SLIME_BLOCK;
                        slimeBlock.onLandedUpon(world, slimeBlock.getDefaultState(), pos, entity, fallDistance);
                        ci.cancel();
                    }
                }
            }
        }
    }

    @Inject(method = "onEntityLand", at = @At(value = "HEAD"), cancellable = true)
    private void bwoots_onEntityLand(BlockView blockView, Entity entity, CallbackInfo ci){
        if ((entity instanceof PlayerEntity playerEntity)) {
            if (!playerEntity.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {
                var meteor = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(ModEnchantments.METEOR);
                if (EnchantmentHelper.getLevel(meteor, playerEntity.getEquippedStack(EquipmentSlot.FEET)) > 0) {
                    if (entity.isSneaking() &&
                            entity.fallDistance >= BwootsConfig.getMeteorMinFallDistance() &&
                            entity.getVelocity().y * entity.getWorld().getTickManager().getTickRate() <= -BwootsConfig.getMeteorMinFallSpeed()) {
                        explode(entity, blockView.getBlockState(entity.getBlockPos()));
                        ci.cancel();
                    }
                }

                var updraft = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(ModEnchantments.UPDRAFT);
                if (EnchantmentHelper.getLevel(updraft, playerEntity.getEquippedStack(EquipmentSlot.FEET)) > 0) {
                    if (entity.isSneaking()) {
                        launch(entity);
                        ci.cancel();
                    }
                }

                var slimy = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(ModEnchantments.SLIMY);
                if (EnchantmentHelper.getLevel(slimy, playerEntity.getEquippedStack(EquipmentSlot.FEET)) > 0) {
                    if (!entity.bypassesLandingEffects()) {
                        bounce(entity);
                        ci.cancel();
                    }
                }
            }
        }
    }

    private void bounce(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < -0.45) {
            double d = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 0.5f, 1.0f);

            if (!entity.getWorld().isClient()) {
                ((ServerWorld)entity.getWorld()).spawnParticles(
                        ParticleTypes.ITEM_SLIME,
                        entity.getPos().x,
                        entity.getPos().y,
                        entity.getPos().z,
                        5,
                        0.2f, 0, 0.2f,
                        1
                );
            }
        } else if (vec3d.y < -0.25){
            entity.setVelocity(vec3d.x, 0.0, vec3d.z);
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 0.25f, 1.0f);

            if (!entity.getWorld().isClient()) {
                ((ServerWorld)entity.getWorld()).spawnParticles(
                        ParticleTypes.ITEM_SLIME,
                        entity.getPos().x,
                        entity.getPos().y,
                        entity.getPos().z,
                        3,
                        0, 0, 0,
                        1
                );
            }
        } else {
            entity.setVelocity(vec3d.x, 0.0, vec3d.z);
        }
    }

    private void launch(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        // for some reason y velocity increases negatively if sneaking on the ground, so sneaking can charge velocity
        if (vec3d.y < -BwootsConfig.getUpdraftMaxCharge()) {
            entity.setVelocity(vec3d.x, Math.min(-vec3d.y, BwootsConfig.getUpdraftMaxCharge()), vec3d.z);
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_BREEZE_SHOOT, SoundCategory.PLAYERS, 0.5f, 1);

            if (!entity.getWorld().isClient()) {
                ((ServerWorld)entity.getWorld()).spawnParticles(
                        ParticleTypes.GUST_EMITTER_SMALL,
                        entity.getPos().x,
                        entity.getPos().y + 0.5,
                        entity.getPos().z,
                        1,
                        0, 0, 0,
                        1
                );
            }
        } else if(vec3d.y < -0.1) {
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_BREEZE_IDLE_GROUND, SoundCategory.PLAYERS, 0.25f, 1);

            if (!entity.getWorld().isClient()) {
                ((ServerWorld)entity.getWorld()).spawnParticles(
                        ParticleTypes.SMALL_GUST,
                        entity.getPos().x,
                        entity.getPos().y + 0.5,
                        entity.getPos().z,
                        1,
                        0.5, 0.5, 0.5,
                        2
                );
            }
        }
    }

    private void explode(Entity entity, BlockState blockState) {
        float fallScaling = ((float)entity.fallDistance - BwootsConfig.getMeteorMinFallDistance()) / BwootsConfig.getMeteorFallDistanceScaling();
        float maxPower = BwootsConfig.getMeteorMaxExplosionPower();

        entity.setVelocity(0, 0, 0);

        if (!entity.getWorld().isClient()) {
            ServerWorld world = (ServerWorld)entity.getWorld();
            world.createExplosion(
                    entity,
                    entity.getDamageSources().explosion(null, entity),
                    new AdvancedExplosionBehavior(
                            true,
                            true,
                            Optional.of(Math.min(1 + fallScaling / 2, 3)),
                            Optional.empty()),
                    entity.getPos(),
                    Math.min(2 + fallScaling, maxPower),
                    false,
                    World.ExplosionSourceType.TNT
            );

            float radius = Math.min(1 + fallScaling, maxPower - 1);
            world.spawnParticles(
                    ParticleTypes.LAVA,
                    entity.getPos().x,
                    entity.getPos().y,
                    entity.getPos().z,
                    10 * (int) radius,
                    radius, 0, radius,
                    1
            );
            world.spawnParticles(
                    ParticleTypes.LARGE_SMOKE,
                    entity.getPos().x,
                    entity.getPos().y,
                    entity.getPos().z,
                    5 * (int) radius,
                    radius, 0, radius,
                    1
            );
            world.spawnParticles(
                    ParticleTypes.LANDING_LAVA,
                    entity.getPos().x,
                    entity.getPos().y,
                    entity.getPos().z,
                    20 * (int) radius,
                    radius, 0, radius,
                    1
            );
            world.spawnParticles(
                    new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, blockState),
                    entity.getPos().x,
                    entity.getPos().y,
                    entity.getPos().z,
                    50 * (int) radius,
                    radius + 1, radius + 1, radius + 1,
                    1
            );
        }
    }
}
