package org.Presentation;


import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.*;
import javafx.collections.ObservableList;
import org.Data.RecordModel;
import org.Data.Serialization;
import org.Logic.Dashboard;
import org.Logic.GearException;
import org.Logic.SpeedThread;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;
import com.diogonunes.jcdp.*;
import org.Logic.TurnSignalException;

import javax.xml.stream.XMLStreamException;

public class TUI extends UIController {
    private SpeedThread speedThread = null;

    Thread keyThread;
    boolean listenKeys = false;

    public static void main(String[] args) {
        Console console = System.console();
//        if(console == null) {
//            System.out.println("Wrong console! You need to execute me in a system console, please");
//            System.exit(0);
//        }
        TUI tui = new TUI();
        tui.drawMainMenu();
    }

    public void KeyListener()
    {
        keyThread = new Thread(){
            public void run()
            {
                char znak;
                Scanner scanner = new Scanner(System.in);
                while(listenKeys)
                {
                    znak = scanner.next().charAt(0);
                    System.out.println((short)(znak-'0'));
                    if(Character.isDigit(znak)) {
                        if(znak>= '0' && znak <= '6') {
                            try {
                                dashboard.setCurrentGear((short) (znak-'0'),true);
                            } catch (GearException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        switch (znak) {
                            //Przyspieszanie/zwalnianie
                            case 'w':
                                dashboard.setKeyUp(true);
                            case 's':
                                dashboard.setKeyUp(false);
                            //Światła
                            case 'z':
                                    dashboard.setPositionLights(!dashboard.isPositionLights());
                                break;
                            case 'x':
                                    dashboard.setLowBeam(!dashboard.isLowBeam());
                                break;
                            case 'c':
                                dashboard.setHighBeam(!dashboard.isHighBeam());
                                break;
                            case 'v':
                                dashboard.setRearFogLights(!dashboard.isBackFogLights());
                                break;
                            case 'b':
                                dashboard.setFrontFogLights(!dashboard.isFrontFogLights());
                                break;
                            case 'n':
                                try {
                                    dashboard.setLeftTurnSignal(!dashboard.isLeftTurnSignal());
                                } catch (TurnSignalException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 'm':
                                try {
                                    dashboard.setRightTurnSignal(!dashboard.isRightTurnSignal());
                                } catch (TurnSignalException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            //Silnik start/stop
                            case 'q':
                                startStopEngine();
                        }
                    }

                    try{
                        keyThread.sleep(100);
                    } catch(Exception e){}
                }
            }
        };
        keyThread.start();
    }

    private static class CLS {
        public static void main(String... arg) throws IOException, InterruptedException {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }

    private void clearTerminal() {
        try {
            CLS.main();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dashboard getDashboard() {
        return dashboard;
    }

    @Override
    public void refresh() {
        clearTerminal();
        System.out.println("=====================================");
        System.out.println("| " + "16:25" + "   " + "Cruise control: Off     "+ dashboard.getCurrentGear() +" |");
        System.out.println("-------------------------------------");
        System.out.println("| avg. speed:" + dashboard.getOnBoardComputer().getAvgSpeed() + "     avg. fuel"+dashboard.getOnBoardComputer().getAvgCombustion()+" |");
        System.out.println("-------------------------------------");
        System.out.println("| speed:       " + dashboard.getSpeed() + "       revs: "+ dashboard.getRevs() +" |");

    }

    private void drawMainMenu() {
        clearTerminal();
        System.out.println("============= Main Menu =============");
        System.out.println("|  1) Drive the car                 |");
        System.out.println("|  2) Import                        |");
        System.out.println("|  3) Export                        |");
        System.out.println("|  4) Settings                      |");
        System.out.println("|  5) Exit                          |");
        System.out.println("=====================================");
        System.out.print("Enter choice: ");
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            case '1':
                startStopEngine();
                break;
            case '2':
                drawImportMenu();
                break;
            case '3':
                drawExportMenu();
                break;
            case '4':
                drawSettingsMenu();
                break;
            case '5':
                try {
                    Serialization.write(dashboard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                break;
            default:
                drawMainMenu();
        }
    }

    private void drawSettingsMenu() {
    }

    private void drawExportMenu() {
        clearTerminal();
        System.out.println("============ Export Menu ============");
        System.out.println("|  1) XML                           |");
        System.out.println("|  2) SQL                           |");
        System.out.println("|  3) Go Back                       |");
        System.out.println("=====================================");
        System.out.println("Enter choice: ");
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            case '1':
                break;
            case '2':
                try {
                    dashboard.writeToDB();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Succesfully exported to database!");
                break;
            case '3':
                drawMainMenu();
                break;
            default:
                drawImportMenu();
        }
    }

    private void drawImportMenu() {
        clearTerminal();
        System.out.println("============ Import Menu ============");
        System.out.println("|  1) XML                           |");
        System.out.println("|  2) SQL                           |");
        System.out.println("|  3) Go Back                       |");
        System.out.println("=====================================");
        System.out.print("Enter choice: ");
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            case '1':
                System.out.println("Enter file path: ");
                try {
                    dashboard.readFromXml(scanner.nextLine());
                } catch (IOException | XMLStreamException e) {
                    e.printStackTrace();
                }
                break;
            case '2':

                break;
            case '3':
                drawMainMenu();
                break;
            default:
                drawImportMenu();
        }
    }

    private int chooseDBRecord() {
        clearTerminal();
        ObservableList<RecordModel> records = dashboard.readFromDB();
        System.out.println("============================================================================================================================================");
        System.out.println("| ID   Avg. speed   Max speed   Avg. fuel   Max fuel   Journey dist.   Journey time   Counter   Day counter 1   Day counter 2  Create date |");
//        for(int i = 0; i < records.size(); i++) {
//            System.out.println("");
//        }
        System.out.print("Enter choice: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if(choice < 0 || choice >= records.size())
            chooseDBRecord();
        return choice;
    }

    @Override
    public void reloadAfterSettings() {
        super.reloadAfterSettings();
    }

    @Override
    public void startStopEngine() {
        if(speedThread == null) {
            listenKeys = true;
            KeyListener();
            speedThread = new SpeedThread(this, 0);
            speedThread.setEngineRunning(true);
            speedThread.start();
        }
        else {
            listenKeys = false;
            speedThread.interrupt();
            speedThread = null;
        }

    }

    @Override
    public void switchEngine(boolean enable, boolean interrupted) {
        super.switchEngine(enable, interrupted);
    }

    @Override
    public void switchOffCruiseControl() {
        super.switchOffCruiseControl();
    }
}
