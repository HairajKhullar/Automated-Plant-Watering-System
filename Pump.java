package eecs1021;

import org.firmata4j.Pin;

import java.io.IOException;

public class Pump {
    private Pin pin;
    public void activatePump(Pin pin) {
        this.pin = pin;
        try {
            pin.setMode(Pin.Mode.OUTPUT);
            pin.setValue(1);
            Thread.sleep(5000);
            pin.setValue(0);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deactivatePump(Pin pin) {
        this.pin = pin;
        try {
            pin.setMode(Pin.Mode.OUTPUT);
            pin.setValue(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}