package eecs1021;

import org.firmata4j.IODevice;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlantWateringSystem{
    private static final String myPort = "COM3";
    static IODevice myArduinoBoard = new FirmataDevice(myPort);
    private static MoistureSensor soilMoistureSensor;

    private static Pump pumpController;
    //private static ButtonController buttonController;
    private static OLEDDisplay displayController;


    private static double MIN_VALUE = 550;
    private static double MAX_VALUE = 730;
    private static final double DRY_THRESHOLD = 705; //find this
    private static final double WET_THRESHOLD = 540; //find this
    private static final double VARIANCE = 20;
    private static final double MOIST_THRESHOLD = 580;


    public static void main(String[] args) throws IOException, InterruptedException {


        initializeTheBoard();

        displayController = new OLEDDisplay(myArduinoBoard);

        soilMoistureSensor = new MoistureSensor(myArduinoBoard, 14);

        pumpController = new Pump();

        //stateMachineHandler = new StateMachineHandler(myArduinoBoard,2);

        //buttonController = new ButtonController(myArduinoBoard,6);

        startloop();

        stopTheBoard();
    }

    public static void startGraphUpdateTimer() {
        Timer graphUpdateTimer = new Timer();

        graphUpdateTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                double x = (int) (System.currentTimeMillis() / 1000);
                double currentMoistureLevel = soilMoistureSensor.readMoistureLevel();
                double y = convertToPercentage(currentMoistureLevel);
                LivePlottingGraph.updateGraph(x,y);
            }
        },0,1000);
    }

    private static void startloop() throws IOException, InterruptedException {

        LivePlottingGraph.createGraph();

        while (true) {
            startGraphUpdateTimer();

            double currentMoistureLevel = soilMoistureSensor.readMoistureLevel();
            System.out.println("Current Moisture Value is: " + currentMoistureLevel);


            LivePlottingGraph.updateGraph(System.currentTimeMillis() / 1000,convertToPercentage(currentMoistureLevel));

            if(currentMoistureLevel > DRY_THRESHOLD - VARIANCE){
                pumpController.activatePump(myArduinoBoard.getPin(7));
                displayController.updateDisplay("WATERING");
            } else if (currentMoistureLevel < WET_THRESHOLD + VARIANCE) {
                pumpController.deactivatePump(myArduinoBoard.getPin(7));
                displayController.updateDisplay("WATERED");
            } else if (currentMoistureLevel >= MOIST_THRESHOLD + VARIANCE) {
                pumpController.activatePump(myArduinoBoard.getPin(7));
                displayController.updateDisplay("WATERING");
            }

            try{
                Thread.sleep(5000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }


        }
    }

    private static Double convertToPercentage(double currentMoistureLevel) {
        currentMoistureLevel = Math.max(Math.min(currentMoistureLevel,MAX_VALUE),MIN_VALUE);
        return 100 - ((currentMoistureLevel - MIN_VALUE) / (MAX_VALUE - MIN_VALUE))*100;
        //return (1 - (currentMoistureLevel - MIN_VALUE)/(MAX_VALUE - MIN_VALUE))* 100;
    }

    private static void stopTheBoard() throws IOException {
        myArduinoBoard.stop();
        System.out.println("Board has stopped running.");
    }

    private static void initializeTheBoard() throws IOException, InterruptedException {
        myArduinoBoard.start();
        myArduinoBoard.ensureInitializationIsDone();
        System.out.println("The Arduino is started.");
    }
}