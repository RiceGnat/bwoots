package moe.ricegnat.bwoots.config;

public class BwootsConfig {
    // TODO implement actual config later

    public static float getUpdraftMaxCharge() {
        return updraftMaxCharge;
    }

    private static float updraftMaxCharge = 2.5f;
    private static float meteorMinFallSpeed = 40;
    private static float meteorMinFallDistance = 20;
    private static float meteorMaxExplosionPower = 12;
    private static float meteorFallDistanceScaling = 20;

    public static float getMeteorMinFallSpeed() {
        return meteorMinFallSpeed;
    }

    public static float getMeteorMinFallDistance() {
        return meteorMinFallDistance;
    }

    public static float getMeteorMaxExplosionPower() {
        return meteorMaxExplosionPower;
    }

    public static float getMeteorFallDistanceScaling() {
        return meteorFallDistanceScaling;
    }
}
