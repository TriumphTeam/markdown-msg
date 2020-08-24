package me.mattstudios.mfmsg.base.internal.color;

/**
 * Rainbow message color
 */
public final class Rainbow implements MessageColor {

    private final float saturation;
    private final float brightness;

    /**
     * Main constructor
     *
     * @param saturation The saturation of the colors
     * @param brightness The brightness of the colors
     */
    public Rainbow(final float saturation, final float brightness) {
        this.saturation = saturation;
        this.brightness = brightness;
    }

    /**
     * Gets the color saturation
     *
     * @return The saturation
     */
    public float getSaturation() {
        return saturation;
    }

    /**
     * Gets the color brightness
     *
     * @return The brightness
     */
    public float getBrightness() {
        return brightness;
    }

}
