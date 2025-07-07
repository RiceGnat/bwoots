package moe.ricegnat.bwoots.enchantment.effect;

import com.mojang.serialization.MapCodec;
import moe.ricegnat.bwoots.Bwoots;
import moe.ricegnat.bwoots.config.BwootsConfig;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public record FlameParticlesEffect() implements EnchantmentLocationBasedEffect {
    public static final MapCodec<FlameParticlesEffect> CODEC = MapCodec.unit(FlameParticlesEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos, boolean newlyApplied) {
        world.spawnParticles(ParticleTypes.TRIAL_SPAWNER_DETECTION,
                pos.x, pos.y, pos.z,
                3,
                0.5,
                0.5,
                0.5,
                0.02
        );

        if (user.fallDistance >= BwootsConfig.getMeteorMinFallDistance() &&
                user.getVelocity().y * world.getTickManager().getTickRate() <= -BwootsConfig.getMeteorMinFallSpeed()) {
            world.spawnParticles(ParticleTypes.FLAME,
                    pos.x, pos.y, pos.z,
                    1,
                    0.5,
                    1,
                    0.5,
                    0.01
            );

            if (world.getTime() % 10 == 0) {
                world.playSound(null,
                        pos.x, pos.y, pos.z,
                        SoundEvents.ENTITY_BLAZE_BURN,
                        SoundCategory.PLAYERS,
                        100, 0.2f);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentLocationBasedEffect> getCodec() {
        return CODEC;
    }
}
