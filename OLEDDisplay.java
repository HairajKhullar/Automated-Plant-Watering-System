package eecs1021;

import org.firmata4j.I2CDevice;
import org.firmata4j.IODevice;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;

public class OLEDDisplay {
    private IODevice device;
    private String info;
    private SSD1306 oledObject;
    public OLEDDisplay(IODevice device) throws InterruptedException, IOException {
        this.device = device;
        I2CDevice i2CDevice = device.getI2CDevice((byte) 0x3C);
        oledObject = new SSD1306(i2CDevice, SSD1306.Size.SSD1306_128_64);
        oledObject.init();
    }

    public void updateDisplay(String info){
        this.info = info;
        oledObject.clear();
        oledObject.getCanvas().setTextsize(2);
        oledObject.getCanvas().drawString(0,0,info);
        oledObject.display();
    }
}