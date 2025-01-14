package eecs1021;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;

import java.io.IOException;

public class MoistureSensor {
    private IODevice device;
    private Pin moisturePin;


    public MoistureSensor(IODevice device, int moisturePin) throws IOException {
        this.device = device;
        this.moisturePin = device.getPin(moisturePin);
        this.moisturePin.setMode(Pin.Mode.ANALOG);
    }

    public double readMoistureLevel() {
        return  this.moisturePin.getValue();
    }

}