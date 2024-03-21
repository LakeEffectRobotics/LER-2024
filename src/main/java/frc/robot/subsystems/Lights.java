package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lights extends SubsystemBase {
    AddressableLED lights1;
    AddressableLED lights2;

    AddressableLEDBuffer lightsBuffer1;
    AddressableLEDBuffer lightsBuffer2;

    public Lights(AddressableLED lights1, AddressableLED lights2) {
        this.lights1 = lights1;
        this.lights2 = lights2;

        lightsBuffer1 = new AddressableLEDBuffer(30);
        lightsBuffer2 = new AddressableLEDBuffer(30);

        lights1.setLength(lightsBuffer1.getLength());
        lights2.setLength(lightsBuffer2.getLength());

        lights1.setData(lightsBuffer1);
        lights2.setData(lightsBuffer2);

        lights1.start();
        lights2.start();
    }

    /**
     * Sets all the lights to a single colour
     * @param red
     * @param green
     * @param blue
     */
    public void setAll(int red, int green, int blue) {
        for (int i = 0; i < lightsBuffer1.getLength(); i++) {
            lightsBuffer1.setRGB(i, red, green, blue);
            lightsBuffer2.setRGB(i, red, green, blue);
        }
    }

    /**
     * Sets a specific light on both strips 
     * @param light specific light on led strip
     * @param red
     * @param green
     * @param blue
     */
    public void setBoth(int light, int red, int green, int blue) {
        lightsBuffer1.setRGB(light, red, green, blue);
        lightsBuffer2.setRGB(light, red, green, blue);
    }

    /**
     * Sets a specific light on light strip 1
     * @param light specific light on led strip
     * @param red
     * @param green
     * @param blue
     */
    public void set1(int light, int red, int green, int blue) {
        lightsBuffer1.setRGB(light, red, green, blue);
    }

    /**
     * Sets a specific light on light strip 2
     * @param light specific light on led strip
     * @param red
     * @param green
     * @param blue
     */
    public void set2(int light, int red, int green, int blue) {
        lightsBuffer2.setRGB(light, red, green, blue);
    }

    /**
     * Turns off both light strips
     */
    public void clear() {
        for (int i = 0; i < lightsBuffer1.getLength(); i++) {
            lightsBuffer1.setRGB(i, 0, 0, 0);
            lightsBuffer2.setRGB(i, 0, 0, 0);
        }
    }
    
}
