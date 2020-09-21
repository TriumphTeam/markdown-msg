package me.mattstudios.mfmsg.base.internal.color.handlers;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * Handles the rainbow colors
 *
 * Original author: https://github.com/Esophose
 */
public final class RainbowHandler {

    private final float hueStep, saturation, brightness;
    private float hue;

    /**
     * Main constructor of the rainbow handler
     *
     * @param totalColors The amount of characters to color
     * @param saturation  The saturation of the colors
     * @param brightness  The brightness of the colors
     */
    public RainbowHandler(int totalColors, float saturation, float brightness) {
        this.hueStep = 1.0F / totalColors;
        this.saturation = saturation;
        this.brightness = brightness;
        this.hue = 0;
    }

    /**
     * Gets the next color of the rainbow
     *
     * @return the next color of the rainbow
     */
    @NotNull
    public String next() {
        Color color = Color.getHSBColor(this.hue, this.saturation, this.brightness);
        this.hue += this.hueStep;
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

}
