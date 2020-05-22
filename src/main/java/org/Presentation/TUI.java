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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import com.diogonunes.jcdp.*;
import org.Logic.TurnSignalException;
import org.w3c.dom.ls.LSOutput;

import javax.xml.stream.XMLStreamException;

public class TUI extends UIController {
    private SpeedThread speedThread = null;
    private Thread keyThread;
    private boolean listenKeys = false;
    private boolean engineRunning = false;
    private final DateTimeFormatter clockFormatter = DateTimeFormatter.ofPattern("HH:mm");

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
                                break;
                            case 's':
                                dashboard.setKeyUp(false);
                                break;
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
                                engineRunning = false;
                                startStopEngine();
                                break;
                        }
                    }

                    try{
                        sleep(100);
                    } catch(Exception ignored){}
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
        System.out.println("=".repeat(50));
        System.out.print("| " + LocalDateTime.now().format(clockFormatter));
        drawCenterText("DASHBOARD", 40);
        System.out.println(dashboard.getCurrentGear() + "  |");
        System.out.println("|"+"-".repeat(48)+"|");
        System.out.print("| Avg. speed: "); drawCenterText(dashboard.getOnBoardComputer().getAvgSpeed(),9);
        System.out.print(" Avg. fuel usg.: ");
        drawCenterText(dashboard.getOnBoardComputer().getAvgCombustion(), 9);
        System.out.println("|");
        System.out.print("| Max. speed: "); drawCenterText(dashboard.getOnBoardComputer().getMaxSpeed(),9);
        System.out.print(" Max. fuel usg.: ");
        drawCenterText(dashboard.getOnBoardComputer().getMaxCombustion(), 9);
        System.out.println(" |");
        System.out.println("|"+"-".repeat(48)+"|");
        System.out.print("|");
        drawCenterText(Math.round(dashboard.getCounter())+"km",48);
        System.out.println("|");
        System.out.println("|"+"-".repeat(48)+"|");
        System.out.print("|");
        drawCenterText(Math.round(dashboard.getDayCounter1()*100f)/100f+" km", 24);
        drawCenterText(Math.round(dashboard.getDayCounter2()*100f)/100f+" km", 24);
        System.out.println("|");
        System.out.println("|"+"-".repeat(48)+"|");
        System.out.print("|  Distance:");
        drawCenterText(Math.round(dashboard.getOnBoardComputer().getJourneyDistance()*100f)/100f+"km", 12);
        System.out.print("Journey time:");
        drawCenterText(dashboard.getOnBoardComputer().getJourneyStartTime(), 12);
        System.out.println(" |");
        System.out.println("|"+"-".repeat(48)+"|");
        System.out.print("|     Speed:");
        drawCenterText(dashboard.getSpeed(), 15);
        System.out.print("    Revs:");
        drawCenterText(dashboard.getRevs(), 15);
        System.out.println("|");
        System.out.println("=".repeat(50));

    }

    private void drawCenterText(Object object, int width) {
        StringBuilder result = new StringBuilder();
        String repeat = " ".repeat(Math.max(0, (width - object.toString().length())/2));
        result.append(repeat);
        result.append(object.toString());
        result.append(repeat);

        System.out.print(result);
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
                engineRunning = true;
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
                System.out.println("Wrong choice!");
                waitForEnter(true);
        }
    }

    private void drawSettingsMenu() {
        clearTerminal();
        System.out.println("============= Settings ==============");
        System.out.println("|  1) Max speed                     |");
        System.out.println("|  2) Engine type                   |");
        System.out.println("|  3) Number of gears               |");
        System.out.println("|  4) Go Back                       |");
        System.out.println("=====================================");
        System.out.println("Enter choice: ");
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);

        switch (choice) {
            case '1':
                short maxSpeed;
                do {
                    System.out.print("Set max speed:");
                    maxSpeed = Short.parseShort(scanner.next());
                    if(maxSpeed < 50 || maxSpeed > 999)
                        System.out.println("Incorrect max speed (Correct value: [50-999])!");
                }while(maxSpeed < 50 || maxSpeed > 999);
                dashboard.getOnBoardComputer().setMaxSpeed(maxSpeed);
                System.out.println("Max speed succesfully set to: "+maxSpeed);
                waitForEnter(false);
                drawSettingsMenu();
                break;
            case '2':
                char engineType;
                do {
                    System.out.println("Choose engine type: ");
                    System.out.println("\t P) Petrol");
                    System.out.println("\t D) Diesel");
                    engineType = scanner.next().charAt(0);
                    if(engineType != 'P' && engineType != 'D')
                        System.out.println("Wrong choice!");
                }while(engineType != 'P' && engineType != 'D');
                dashboard.getSettings().setEngineType(engineType);
                System.out.println("Engine type succesfully set to: "+((engineType == 'P') ? "Petrol" : "Diesel"));
                waitForEnter(false);
                drawSettingsMenu();
                break;
            case '3':
                char gear;
                do {
                    System.out.println("Choose number of gears:");
                    System.out.println("\t 5) 5 Gears");
                    System.out.println("\t 6) 6 Gears");
                    gear = scanner.next().charAt(0);
                    if(gear != '5' && gear != '6')
                        System.out.println("Wrong choice!");
                }while(gear != '5' && gear != '6');
                dashboard.getSettings().setNumberOfGears((byte)(gear-'0'));
                System.out.println("Number of gears succesfully set to: "+gear);
                waitForEnter(false);
                drawSettingsMenu();
                break;
            case '4':
                drawMainMenu();
                break;
            default:
                System.out.println("Wrong choice!");
                waitForEnter(false);
                drawSettingsMenu();
        }
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
                    System.out.println("Enter file path: ");
                    try {
                        dashboard.writeToXml(scanner.next());
                        System.out.println("Succesfully exported to XML file!");
                        waitForEnter(true);
                    } catch (IOException | XMLStreamException e) {
                        System.out.println("Export failed!\n" + e.getMessage());
                        waitForEnter(false);
                        drawExportMenu();
                    }
                break;
            case '2':
                    try {
                        dashboard.writeToDB();
                        System.out.println("Succesfully exported to database!");
                        waitForEnter(true);
                    } catch (Exception e) {
                        System.out.println("Export failed!\n" + e.getMessage());
                        waitForEnter(false);
                        drawExportMenu();
                    }
                break;
            case '3':
                    drawMainMenu();
                break;
            default:
                System.out.println("Wrong choice!");
                waitForEnter(false);
                drawExportMenu();
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
                        dashboard.readFromXml(scanner.next());
                        System.out.println("Succesfully imported dashboard data from XML file!");
                        waitForEnter(true);
                    } catch (IOException | XMLStreamException e) {
                        System.out.println("Import failed!\n" + e.getMessage());
                        waitForEnter(false);
                        drawImportMenu();
                    }
                break;
            case '2':
                    dashboard.updateDashboard(chooseDBRecord());
                    System.out.println("Succesfully imported dashboard data from database!");
                    waitForEnter(true);
                break;
            case '3':
                    drawMainMenu();
                break;
            default:
                System.out.println("Wrong choice!");
                waitForEnter(false);
                drawImportMenu();
        }
    }

    private void waitForEnter(boolean goToMainMenu) {
        System.out.println("\nPress enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if(goToMainMenu)
            drawMainMenu();
    }

    private RecordModel chooseDBRecord() {
        clearTerminal();
        ObservableList<RecordModel> records = dashboard.readFromDB();
        System.out.println("================================================================================================================================================");
        System.out.println("|   ID   Avg. speed   Max speed   Avg. fuel   Max fuel   Journey dist.   Journey time   Counter   Day counter 1   Day counter 2  Create date   |");

        for(int i = 0; i < records.size(); i++) {
            System.out.print("| ");
            drawTableCell(records.get(i).getId(),4);
            drawTableCell(records.get(i).getAvgSpeed(),13);
            drawTableCell(records.get(i).getMaxSpeed(),12);
            drawTableCell(records.get(i).getAvgFuel(),12);
            drawTableCell(records.get(i).getMaxFuel(), 11);
            drawTableCell(records.get(i).getJourneyDistance(), 16);
            drawTableCell(records.get(i).getJourneyTime(), 15);
            drawTableCell(records.get(i).getCounter(), 10);
            drawTableCell(records.get(i).getDayCounter1(), 16);
            drawTableCell(records.get(i).getDayCounter2(), 16);
            drawTableCell(records.get(i).getCreateDate(), 13);
            System.out.print("   |\n");
        }
        System.out.println("================================================================================================================================================");
        System.out.println("\t0) Cancel \n");
        System.out.print("Choice record: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if(choice == 0)
            drawImportMenu();
        else if(choice < 0 || choice >= records.size()) {
            System.out.println();
            waitForEnter(false);
            chooseDBRecord();
        }
        return records.get(choice-1);
    }

    private void drawTableCell(Object cell, int columnWidth) {
        StringBuilder result = new StringBuilder();
        String repeat = " ".repeat(Math.max(0, columnWidth - cell.toString().length()));
        result.append(repeat);
        result.append(cell.toString());

        System.out.print(result);
    }

    @Override
    public void reloadAfterSettings() {
        super.reloadAfterSettings();
    }

    @Override
    public void startStopEngine() {
        if(engineRunning) {
            listenKeys = true;
            KeyListener();
            speedThread = new SpeedThread(this, 0);
            speedThread.setEngineRunning(true);
            speedThread.start();
        }
        else {
            listenKeys = false;
            speedThread.setEngineRunning(false);
            speedThread.interrupt();
            speedThread = null;
            drawMainMenu();
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
