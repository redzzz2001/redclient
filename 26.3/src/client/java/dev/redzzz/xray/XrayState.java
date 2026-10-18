package dev.redzzz.xray;

public final class XrayState {
    private static volatile boolean nightVisionEnabled;
    private static volatile boolean playerEspEnabled;
    private static volatile boolean chestEspEnabled;
    private static volatile boolean bedEspEnabled;
    private static volatile boolean aimBotEnabled;
    private static volatile float aimStrength = 0.35F;
    private static volatile boolean killAuraEnabled;
    private static volatile boolean flyEnabled;
    private static volatile boolean jesusEnabled;
    private static volatile boolean autoTotemEnabled;
    private static volatile boolean mobEspEnabled;
    private static volatile boolean pvpCrosshairEnabled = true;
    private static volatile boolean noFallEnabled;
    private static volatile boolean criticalsEnabled;
    private static volatile boolean speedEnabled;
    private static volatile boolean superJumpEnabled;
    private static volatile boolean friendProtectEnabled;
    private static volatile boolean autoLootEnabled;
    private static volatile boolean triggerBotEnabled;
    private static volatile boolean reachEnabled;
    private static volatile boolean fastMineEnabled;
    private static volatile float reachDistance = 4.5F;
    private static volatile float aimRange = 64.0F;
    private static volatile float killAuraRange = 4.2F;
    private static volatile float triggerStrength = 0.92F;
    private static volatile float flySpeed = 0.05F;
    private static volatile float speedMultiplier = 1.38F;
    private static volatile float jumpPower = 0.92F;
    private static volatile int autoLootDelay = 2;
    private static volatile float fastMineMultiplier = 1.30F;
    private static volatile float entityEspRange = 96.0F;
    private static volatile int blockEspChunkRadius = 8;

    private XrayState() {}

    public static boolean isNightVisionEnabled() {
        return nightVisionEnabled;
    }

    public static boolean toggleNightVision() {
        nightVisionEnabled = !nightVisionEnabled;
        return nightVisionEnabled;
    }

    public static boolean isPlayerEspEnabled() {
        return playerEspEnabled;
    }

    public static void togglePlayerEsp() {
        playerEspEnabled = !playerEspEnabled;
    }

    public static boolean isChestEspEnabled() {
        return chestEspEnabled;
    }

    public static void toggleChestEsp() {
        chestEspEnabled = !chestEspEnabled;
    }

    public static boolean isBedEspEnabled() {
        return bedEspEnabled;
    }

    public static void toggleBedEsp() {
        bedEspEnabled = !bedEspEnabled;
    }

    public static boolean isAimBotEnabled() {
        return aimBotEnabled;
    }

    public static void toggleAimBot() {
        aimBotEnabled = !aimBotEnabled;
    }

    public static float getAimStrength() {
        return aimStrength;
    }

    public static void setAimStrength(float strength) {
        aimStrength = Math.max(0.1F, Math.min(1.0F, strength));
    }

    public static boolean isKillAuraEnabled() {
        return killAuraEnabled;
    }

    public static void toggleKillAura() {
        killAuraEnabled = !killAuraEnabled;
    }

    public static boolean isFlyEnabled() {
        return flyEnabled;
    }

    public static void toggleFly() {
        flyEnabled = !flyEnabled;
    }

    public static boolean isJesusEnabled() {
        return jesusEnabled;
    }

    public static void toggleJesus() {
        jesusEnabled = !jesusEnabled;
    }

    public static boolean isAutoTotemEnabled() {
        return autoTotemEnabled;
    }

    public static void toggleAutoTotem() {
        autoTotemEnabled = !autoTotemEnabled;
    }

    public static boolean isMobEspEnabled() {
        return mobEspEnabled;
    }

    public static void toggleMobEsp() {
        mobEspEnabled = !mobEspEnabled;
    }

    public static boolean isPvpCrosshairEnabled() {
        return pvpCrosshairEnabled;
    }

    public static void togglePvpCrosshair() {
        pvpCrosshairEnabled = !pvpCrosshairEnabled;
    }

    public static boolean isNoFallEnabled() {
        return noFallEnabled;
    }

    public static void toggleNoFall() {
        noFallEnabled = !noFallEnabled;
    }

    public static boolean isCriticalsEnabled() {
        return criticalsEnabled;
    }

    public static void toggleCriticals() {
        criticalsEnabled = !criticalsEnabled;
    }

    public static boolean isSpeedEnabled() {
        return speedEnabled;
    }

    public static void toggleSpeed() {
        speedEnabled = !speedEnabled;
    }

    public static boolean isSuperJumpEnabled() {
        return superJumpEnabled;
    }

    public static void toggleSuperJump() {
        superJumpEnabled = !superJumpEnabled;
    }

    public static boolean isFriendProtectEnabled() {
        return friendProtectEnabled;
    }

    public static void toggleFriendProtect() {
        friendProtectEnabled = !friendProtectEnabled;
    }

    public static boolean isAutoLootEnabled() {
        return autoLootEnabled;
    }

    public static void toggleAutoLoot() {
        autoLootEnabled = !autoLootEnabled;
    }

    public static boolean isTriggerBotEnabled() {
        return triggerBotEnabled;
    }

    public static void toggleTriggerBot() {
        triggerBotEnabled = !triggerBotEnabled;
    }

    public static boolean isReachEnabled() {
        return reachEnabled;
    }

    public static void toggleReach() {
        reachEnabled = !reachEnabled;
    }

    public static float getReachDistance() {
        return reachDistance;
    }

    public static void setReachDistance(float distance) {
        reachDistance = Math.max(3.0F, Math.min(10.0F, distance));
    }

    public static boolean isFastMineEnabled() {
        return fastMineEnabled;
    }

    public static void toggleFastMine() {
        fastMineEnabled = !fastMineEnabled;
    }

    public static float getAimRange() { return aimRange; }
    public static void setAimRange(float value) { aimRange = clamp(value, 3.0F, 64.0F); }

    public static float getKillAuraRange() { return killAuraRange; }
    public static void setKillAuraRange(float value) { killAuraRange = clamp(value, 3.0F, 10.0F); }

    public static float getTriggerStrength() { return triggerStrength; }
    public static void setTriggerStrength(float value) { triggerStrength = clamp(value, 0.1F, 1.0F); }

    public static float getFlySpeed() { return flySpeed; }
    public static void setFlySpeed(float value) { flySpeed = clamp(value, 0.05F, 0.50F); }

    public static float getSpeedMultiplier() { return speedMultiplier; }
    public static void setSpeedMultiplier(float value) { speedMultiplier = clamp(value, 1.0F, 3.0F); }

    public static float getJumpPower() { return jumpPower; }
    public static void setJumpPower(float value) { jumpPower = clamp(value, 0.42F, 1.50F); }

    public static int getAutoLootDelay() { return autoLootDelay; }
    public static void setAutoLootDelay(int value) { autoLootDelay = Math.max(0, Math.min(10, value)); }

    public static float getFastMineMultiplier() { return fastMineMultiplier; }
    public static void setFastMineMultiplier(float value) { fastMineMultiplier = clamp(value, 1.0F, 1.40F); }

    public static float getEntityEspRange() { return entityEspRange; }
    public static void setEntityEspRange(float value) { entityEspRange = clamp(value, 16.0F, 128.0F); }

    public static int getBlockEspChunkRadius() { return blockEspChunkRadius; }
    public static void setBlockEspChunkRadius(int value) { blockEspChunkRadius = Math.max(1, Math.min(12, value)); }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}
