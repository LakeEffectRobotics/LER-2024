package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lights extends SubsystemBase {
    AddressableLED leftLights;
    AddressableLED rightLights;

    AddressableLEDBuffer leftBuffer;
    AddressableLEDBuffer rightBuffer;
    
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public Lights(AddressableLED lights1, AddressableLED lights2) {
        this.leftLights = lights1;
        this.rightLights = lights2;

        leftBuffer = new AddressableLEDBuffer(30);
        rightBuffer = new AddressableLEDBuffer(30);

        lights1.setLength(leftBuffer.getLength());
        lights2.setLength(rightBuffer.getLength());

        lights1.setData(leftBuffer);
        lights2.setData(rightBuffer);

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
        for (int i = 0; i < leftBuffer.getLength(); i++) {
            leftBuffer.setRGB(i, red, green, blue);
            rightBuffer.setRGB(i, red, green, blue);
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
        leftBuffer.setRGB(light, red, green, blue);
        rightBuffer.setRGB(light, red, green, blue);
    }

    /**
     * Sets the specified light on the specific side
     * @param side left or right
     * @param light specific light on strip 
     * @param red
     * @param green
     * @param blue
     */
    public void set(int side, int light, int red, int green, int blue) {
        if (side == LEFT) {
            leftBuffer.setRGB(light, red, green, blue);
        } else if (side == RIGHT) {
            rightBuffer.setRGB(light, red, green, blue);
        } else {
            return;
        }
        
    }

    /**
     * Turns off both light strips
     */
    public void clear() {
        for (int i = 0; i < leftBuffer.getLength(); i++) {
            leftBuffer.setRGB(i, 0, 0, 0);
            rightBuffer.setRGB(i, 0, 0, 0);
        }
    }
    
}
