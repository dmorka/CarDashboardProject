package org.Presentation;


import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.FColor;
import javafx.collections.ObservableList;
import org.Data.RecordModel;
import org.Data.Serialization;
import org.Logic.Dashboard;
import org.Logic.GearException;
import org.Logic.SpeedThread;
import org.Logic.TurnSignalException;

import javax.xml.stream.XMLStreamException;
import java.io.Console;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The type Tui.
 */
public class TUI extends UIController {
    private final DateTimeFormatter clockFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final ColoredPrinter purpleTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.MAGENTA)
            .build();
    private final ColoredPrinter blueTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.BLUE)
            .build();
    private final ColoredPrinter yellowTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.YELLOW)
            .build();
    private final ColoredPrinter cyanTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.CYAN)
            .build();
    private final ColoredPrinter greenTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.GREEN)
            .build();
    private final ColoredPrinter redTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.RED)
            .build();
    private final ColoredPrinter blackTextColor = new ColoredPrinter.Builder(0, false)
            .foreground(FColor.BLACK)
            .build();
    private SpeedThread speedThread = null;
    private Thread keyThread = null;
    //    private boolean keyListen = false;
    private AtomicBoolean keyListen = new AtomicBoolean(false);
    private boolean engineRunning = false;
    private long turnSignalTime = System.currentTimeMillis();
    private ColoredPrinter rightTurnSignalColor = blackTextColor,
            leftTurnSignalColor = blackTextColor,
            parkingLightColor = blackTextColor,
            lowBeamColor = blackTextColor,
            highBeamColor = blackTextColor,
            fogFrontColor = blackTextColor,
            fogBackColor = blackTextColor;


    /**
     * Instantiates a new Tui.
     */
    public TUI() {
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.out.println("Wrong console! You need to execute me in a system console, please");
            System.exit(0);
        }
        TUI tui = new TUI();
        tui.drawMainMenu();
    }

    /**
     * Key listener.
     *
     * @param listen the listen
     */
    public void KeyListener(boolean listen) {
//        Scanner scanner = new Scanner(System.in);
        if (listen) {
            keyThread = new Thread() {
                public void run() {
                    char znak;
                    Scanner scanner = new Scanner(System.in);
                    while (keyListen.get()) {
                        znak = scanner.next().charAt(0);
                        if (Character.isDigit(znak)) {
                            if (znak >= '0' && znak <= '6') {
                                try {
                                    dashboard.setCurrentGear((short) (znak - '0'), true);
                                } catch (GearException ignore) {
                                }
                            }
                        } else {
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
                                    parkingLightColor = (dashboard.isPositionLights()) ? greenTextColor : blackTextColor;
                                    break;
                                case 'x':
                                    dashboard.setLowBeam(!dashboard.isLowBeam());
                                    lowBeamColor = (dashboard.isLowBeam()) ? greenTextColor : blackTextColor;
                                    break;
                                case 'c':
                                    dashboard.setHighBeam(!dashboard.isHighBeam());
                                    highBeamColor = (dashboard.isHighBeam()) ? blueTextColor : blackTextColor;
                                    break;
                                case 'v':
                                    dashboard.setRearFogLights(!dashboard.isBackFogLights());
                                    fogBackColor = (dashboard.isBackFogLights()) ? yellowTextColor : blackTextColor;
                                    break;
                                case 'b':
                                    dashboard.setFrontFogLights(!dashboard.isFrontFogLights());
                                    fogFrontColor = (dashboard.isFrontFogLights()) ? yellowTextColor : blackTextColor;
                                    break;
                                case 'n':
                                    try {
                                        dashboard.setLeftTurnSignal(!dashboard.isLeftTurnSignal());
                                    } catch (TurnSignalException ignore) {
                                    }
                                    break;
                                case 'm':
                                    try {
                                        dashboard.setRightTurnSignal(!dashboard.isRightTurnSignal());
                                    } catch (TurnSignalException ignore) {
                                    }
                                    break;
                                //Silnik start/stop
                                case 'q':
                                    engineRunning = false;
                                    startStopEngine();
                                    break;
                            }
                        }

                        try {
                            sleep(100);
                        } catch (Exception ignored) {
                        }
                    }
                }
            };
//            keyListen = true;
            keyListen.set(true);
            keyThread.start();
        } else if (keyThread != null) {
//            keyListen = false;
            keyListen.set(false);
            //scanner.reset();
            keyThread = null;
        }
    }

    private void clearTerminal() {
        try {
            CLS.main();
        } catch (IOException | InterruptedException ignore) {
            //drawMainMenu();
        }
    }

    @Override
    public Dashboard getDashboard() {
        return dashboard;
    }

    @Override
    public void refresh() {
        clearTerminal();
        purpleTextColor.println("\n\t" + "=".repeat(50) + "");
        purpleTextColor.print("\t|" + " ".repeat(48) + "|\n\t| ");
        cyanTextColor.print(LocalDateTime.now().format(clockFormatter));
        drawCenterText("DASHBOARD  ", 39, yellowTextColor);
        cyanTextColor.print(dashboard.getCurrentGear());
        purpleTextColor.println("  |\n\t|" + " ".repeat(48) + "|");
        purpleTextColor.println("\t|" + "-".repeat(48) + "|");
        purpleTextColor.print("\t|" + " ".repeat(48) + "|\n\t| ");
        blueTextColor.print("Avg. speed: ");
        drawCenterText(dashboard.getOnBoardComputer().getAvgSpeed(), 9, cyanTextColor);
        blueTextColor.print("  Avg. fuel usg.: ");
        drawCenterText(Math.round(dashboard.getOnBoardComputer().getAvgCombustion() * 10f) / 10f, 9, cyanTextColor);
        purpleTextColor.print("|\n\t| ");
        blueTextColor.print("Max. speed: ");
        drawCenterText(dashboard.getOnBoardComputer().getMaxSpeed(), 9, cyanTextColor);
        blueTextColor.print(" Max. fuel usg.: ");
        drawCenterText(Math.round(dashboard.getOnBoardComputer().getMaxCombustion() * 10f) / 10f, 9, cyanTextColor);
        purpleTextColor.println(" |\n\t|" + " ".repeat(48) + "|");
        purpleTextColor.println("\t|" + "-".repeat(48) + "|");
        purpleTextColor.print("\t|" + " ".repeat(48) + "|\n\t|");
        drawCenterText(Math.round(dashboard.getCounter()) + "km", 48, cyanTextColor);
        purpleTextColor.println("|\n\t|" + " ".repeat(48) + "|");
        purpleTextColor.println("\t|" + "-".repeat(48) + "|");
        purpleTextColor.print("\t|" + " ".repeat(48) + "|\n\t|");
        drawCenterText(Math.round(dashboard.getDayCounter1() * 10f) / 10f + " km ", 24, cyanTextColor);
        drawCenterText(Math.round(dashboard.getDayCounter2() * 10f) / 10f + " km ", 24, cyanTextColor);
        purpleTextColor.println("|\n\t|" + " ".repeat(48) + "|");
        purpleTextColor.println("\t|" + "-".repeat(48) + "|");
        purpleTextColor.print("\t|" + " ".repeat(48) + "|\n\t|  ");
        blueTextColor.print("Distance:");
        drawCenterText(Math.round(dashboard.getOnBoardComputer().getJourneyDistance() * 10f) / 10f + "km", 12, cyanTextColor);
        blueTextColor.print("Journey time:");
        drawCenterText(dashboard.getOnBoardComputer().getJourneyStartTime(), 12, cyanTextColor);
        purpleTextColor.println(" |\n\t|" + " ".repeat(48) + "|");
        purpleTextColor.println("\t|" + "-".repeat(48) + "|");
        purpleTextColor.print("\t|" + " ".repeat(48) + "|\n\t|     ");
        if (dashboard.isLeftTurnSignal()) {
            if (System.currentTimeMillis() - turnSignalTime > 500) {
                leftTurnSignalColor = (leftTurnSignalColor.equals(blackTextColor)) ? greenTextColor : blackTextColor;
                turnSignalTime = System.currentTimeMillis();
            }
        } else
            leftTurnSignalColor = blackTextColor;

        leftTurnSignalColor.print("LT    ");

        parkingLightColor.print("PT    ");
        lowBeamColor.print("LB    ");
        highBeamColor.print("HB    ");
        fogBackColor.print("FB    ");
        fogFrontColor.print("FF    ");
        if (dashboard.isRightTurnSignal()) {
            if (System.currentTimeMillis() - turnSignalTime > 500) {
                rightTurnSignalColor = (rightTurnSignalColor.equals(blackTextColor)) ? greenTextColor : blackTextColor;
                turnSignalTime = System.currentTimeMillis();
            }
        } else
            rightTurnSignalColor = blackTextColor;

        rightTurnSignalColor.print("RT");
        purpleTextColor.println("     |\n\t|" + " ".repeat(48) + "|");
        purpleTextColor.println("\t|" + "-".repeat(48) + "|");
        purpleTextColor.print("\t|" + " ".repeat(48) + "|\n\t|     ");
        blueTextColor.print("Speed:");
        drawCenterText(dashboard.getSpeed(), 15, cyanTextColor);
        blueTextColor.print("    Revs:");
        drawCenterText(dashboard.getRevs(), 15, cyanTextColor);
        purpleTextColor.println("|\n\t|" + " ".repeat(48) + "|");
        purpleTextColor.println("\t" + "=".repeat(50));
    }

    private void drawCenterText(Object object, int width, ColoredPrinter coloredPrinter) {
        StringBuilder result = new StringBuilder();
        String repeat = " ".repeat(Math.max(0, (width - object.toString().length()) / 2));
        result.append(repeat);
        result.append(object.toString());
        result.append(repeat);

        coloredPrinter.print(result);
    }

    private void drawMainMenu() {
        clearTerminal();
        drawMenu("Main Menu", new String[]{"Drive the car", "Import", "Export", "Settings","Instruction", "About", "Exit"});
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
                drawInstruction();
                break;
            case '6':
                drawAbout();
                break;
            case '7':
                try {
                    Serialization.write(dashboard);
                } catch (IOException e) {
                    redTextColor.println("\t" + e.getMessage());
                }
                System.exit(0);
                break;
            default:
                redTextColor.println("\n\tWrong choice!");
                waitForEnter(true);
        }
    }

    private void drawInstruction() {
        clearTerminal();
        purpleTextColor.print("\n\t+---------------------------- ");
        yellowTextColor.print("Instructions");
        purpleTextColor.print(" ----------------------------+\n\t" +
                "|                                                                      |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|   ");
        cyanTextColor.print("|q| - Engine off                   |n| - Left turn Signal On/Off");
        purpleTextColor.print("   |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|                                                                      |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|   ");
        cyanTextColor.print("|w| - Accelerate                   |m| - Right turn Signal On/Off");
        purpleTextColor.print("  |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|                                                                      |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|   ");
        cyanTextColor.print("|s| - Decelerate                   |v| - Back Fog lights On/Off");
        purpleTextColor.print("    |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|                                                                      |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|   ");
        cyanTextColor.print("|z| - ParkingalightsfOn/Off        |b| - Front Fog lights On/Off");
        purpleTextColor.print("   |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|                                                                      |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|   ");
        cyanTextColor.print("|x| - Low Beam On/Off              |c| - High Beam On/Off");
        purpleTextColor.print("          |\n\t" +
                "|   ");
        blueTextColor.print("+-+                                +-+");
        purpleTextColor.print("                             |\n\t" +
                "|                                                                      |\n\t" +
                "|   ");
        blueTextColor.print("+-+ +-+ +-+ +-+ +-+ +-+");
        purpleTextColor.print("                                            |\n\t" +
                "|   ");
        cyanTextColor.print("|1| |2| |3| |4| |5| |6| - Gears");
        purpleTextColor.print("                                    |\n\t" +
                "|   ");
        blueTextColor.print("+-+ +-+ +-+ +-+ +-+ +-+");
        purpleTextColor.println("                                            |\n\t" +
                "|                                                                      |\n\t" +
                "+----------------------------------------------------------------------+");
        waitForEnter(true);
    }

    private void drawAbout() {
        clearTerminal();
        purpleTextColor.print("\n\n\t+----------------------------------------------------------------------------------------------------------+\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|         ");
        yellowTextColor.print("/$$$$$$$                      /$$       /$$                                           /$$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$__  $$                    | $$      | $$                                          | $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  \\ $$  /$$$$$$   /$$$$$$$| $$$$$$$ | $$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$ |____  $$ /$$_____/| $$__  $$| $$__  $$ /$$__  $$ |____  $$ /$$__  $$ /$$__  $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$  /$$$$$$$|  $$$$$$ | $$  \\ $$| $$  \\ $$| $$  \\ $$  /$$$$$$$| $$  \\__/| $$  | $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$  | $$ /$$__  $$ \\____  $$| $$  | $$| $$  | $$| $$  | $$ /$$__  $$| $$      | $$  | $$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("| $$$$$$$/|  $$$$$$$ /$$$$$$$/| $$  | $$| $$$$$$$/|  $$$$$$/|  $$$$$$$| $$      |  $$$$$$$");
        purpleTextColor.print("        |\n\t|        ");
        yellowTextColor.print("|_______/  \\_______/|_______/ |__/  |__/|_______/  \\______/  \\_______/|__/       \\_______/");
        purpleTextColor.print("        |\n\t|                                                                                                          |\n" +
                "\t|                                                                                     ");
        redTextColor.print("Version 1.0.0");
        purpleTextColor.print("        |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                                          |\n" +
                "\t|                                                                                      ");
        blueTextColor.print("+---------------+");
        purpleTextColor.print("   |\n\t|                                                                                      ");
        cyanTextColor.print("|A|u|t|o|r|z|y|:|");
        purpleTextColor.print("   |\n\t|                                                      ");
        blueTextColor.print("+---------------------------------+ +-----------+");
        purpleTextColor.print("   |\n\t|                                                      ");
        cyanTextColor.print("|K|a|c|p|e|r| |W|l|o|d|a|r|c|z|y|k| |2|2|4|4|5|6|");
        purpleTextColor.print("   |\n\t|                                                      ");
        blueTextColor.print("+-----------------------------------------------+");
        purpleTextColor.print("   |\n\t|                                                                  ");
        cyanTextColor.print("|D|a|w|i|d| |M|o|r|k|a| |2|2|4|3|7|9|");
        purpleTextColor.print("   |\n\t|                                                                  ");
        blueTextColor.print("+---------+ +---------+ +-----------+");
        purpleTextColor.println("   |\n" +
                "\t|                                                                                                          |\n" +
                "\t+----------------------------------------------------------------------------------------------------------+");
        System.out.println("\n");
        waitForEnter(true);
    }

    private void drawMenu(String title, String[] options) {
        int width = 36;
        int len = title.length();
        String repeat = "=".repeat((width - len) / 2);
        purpleTextColor.print("\n\t" + repeat);
        yellowTextColor.print(" " + title + " ");
        purpleTextColor.println(repeat);
        width = 2 * repeat.length() + len + 2;
        purpleTextColor.println("\t|" + " ".repeat(width - 2) + "|");
        for (int i = 0; i < options.length; i++) {
            purpleTextColor.print("\t|  ");
            blueTextColor.print((i + 1) + ") ");
            cyanTextColor.print(options[i]);
            purpleTextColor.println(" ".repeat(width - 7 - options[i].length()) + "|");
        }
        purpleTextColor.println("\t|" + " ".repeat(width - 2) + "|");
        purpleTextColor.println("\t" + "=".repeat(width));
        yellowTextColor.print("\n\tEnter choice: ");
    }

    private void drawSettingsMenu() {
        clearTerminal();
        drawMenu("Settings", new String[]{"Max speed", "Engine type", "Number of gears", "Go back"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);

        switch (choice) {
            case '1':
                short maxSpeed = 0;
                String pom;
                do {
                    yellowTextColor.print("\n\tSet max speed: ");
                    try {
                        maxSpeed = Short.parseShort(scanner.next());
                    } catch (NumberFormatException e) {
                        maxSpeed = 0;
                    }
                    if (maxSpeed < 50 || maxSpeed > 999)
                        redTextColor.println("\tIncorrect max speed (Correct value: [50-999])!");
                } while (maxSpeed < 50 || maxSpeed > 999);
                dashboard.getSettings().setMaxSpeed(maxSpeed);
                dashboard.setGears();
                greenTextColor.println("\n\tMax speed succesfully set to: " + maxSpeed);
                waitForEnter(false);
                drawSettingsMenu();
                break;
            case '2':
                char engineType;
                do {
                    drawMenu("Choose engine type:", new String[]{"Petrol", "Diesel"});
                    engineType = scanner.next().charAt(0);
                    if (engineType != '1' && engineType != '2')
                        redTextColor.println("\n\tWrong choice!");
                } while (engineType != '1' && engineType != '2');
                dashboard.getSettings().setEngineType((engineType == '1') ? 'P' : 'D');
                greenTextColor.println("\n\tEngine type succesfully set to: " + ((engineType == '1') ? "Petrol" : "Diesel"));
                waitForEnter(false);
                drawSettingsMenu();
                break;
            case '3':
                char gear;
                do {
                    drawMenu("Choose number of gears:", new String[]{"5 Gears", "6 Gears"});
                    gear = scanner.next().charAt(0);
                    if ((gear != '1' && gear != '2') && (gear != '5' && gear != '6'))
                        redTextColor.println("\n\tWrong choice!");
                } while ((gear != '1' && gear != '2') && (gear != '5' && gear != '6'));
                if (gear == '5' || gear == '6') {
                    dashboard.getSettings().setNumberOfGears((byte) (gear - '0'));
                    greenTextColor.println("\n\tNumber of gears succesfully set to: " + gear);
                } else {
                    dashboard.getSettings().setNumberOfGears((byte) ((gear == '1') ? 5 : 6));
                    greenTextColor.println("\n\tNumber of gears succesfully set to: " + ((gear == '1') ? 5 : 6));
                }
                dashboard.setGears();

                waitForEnter(false);
                drawSettingsMenu();
                break;
            case '4':
                drawMainMenu();
                break;
            default:
                redTextColor.println("\n\tWrong choice!");
                waitForEnter(false);
                drawSettingsMenu();
        }
    }

    private void drawExportMenu() {
        clearTerminal();
        drawMenu("Export Menu", new String[]{"XML", "SQL", "Go back"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            case '1':
                yellowTextColor.println("\n\tEnter file path: ");
                try {
                    System.out.print("\t");
                    dashboard.writeToXml(scanner.next());
                    greenTextColor.println("\n\tSuccesfully exported to XML file!");
                    waitForEnter(true);
                } catch (IOException | XMLStreamException e) {
                    redTextColor.println("\n\tExport failed!\n\t" + e.getMessage());
                    waitForEnter(false);
                    drawExportMenu();
                }
                break;
            case '2':
                try {
                    dashboard.writeToDB();
                    greenTextColor.println("\n\tSuccesfully exported to database!");
                    waitForEnter(true);
                } catch (Exception e) {
                    redTextColor.println("\n\tExport failed!\n\t" + e.getMessage());
                    waitForEnter(false);
                    drawExportMenu();
                }
                break;
            case '3':
                drawMainMenu();
                break;
            default:
                redTextColor.println("\n\tWrong choice!");
                waitForEnter(false);
                drawExportMenu();
        }
    }

    private void drawImportMenu() {
        clearTerminal();
        drawMenu("Import Menu", new String[]{"XML", "SQL", "Go back"});
        Scanner scanner = new Scanner(System.in);
        char choice = scanner.next().charAt(0);
        switch (choice) {
            case '1':
                yellowTextColor.println("\n\tEnter file path: ");
                try {
                    System.out.print("\t");
                    dashboard.readFromXml(scanner.next());
                    greenTextColor.println("\n\tSuccesfully imported dashboard data from XML file!");
                    waitForEnter(true);
                } catch (IOException | XMLStreamException e) {
                    redTextColor.println("\n\tImport failed!\n\t" + e.getMessage());
                    waitForEnter(false);
                    drawImportMenu();
                }
                break;
            case '2':
                dashboard.updateDashboard(chooseDBRecord());
                greenTextColor.println("\n\tSuccesfully imported dashboard data from database!");
                waitForEnter(true);
                break;
            case '3':
                drawMainMenu();
                break;
            default:
                redTextColor.println("\n\tWrong choice!");
                waitForEnter(false);
                drawImportMenu();
        }
    }

    private void waitForEnter(boolean goToMainMenu) {
        yellowTextColor.println("\n\tPress enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            redTextColor.println("\t" + e.getMessage());
        }
        if (goToMainMenu)
            drawMainMenu();
    }

    private RecordModel chooseDBRecord() {
        clearTerminal();
        ObservableList<RecordModel> records = dashboard.readFromDB();
        purpleTextColor.print("\n\t" + "=".repeat(62));
        yellowTextColor.print(" Dashboard History ");
        purpleTextColor.print("=".repeat(63) + "\n\t|" + " ".repeat(142) + "|" + "\n\t|");
        blueTextColor.print("   ID   Avg. speed   Max speed   Avg. fuel   Max fuel   Journey dist.   Journey time   Counter   Day counter 1   Day counter 2  Create date   ");

        purpleTextColor.println("|");
        for (int i = 0; i < records.size(); i++) {
            purpleTextColor.print("\t| ");
            drawTableCell(i + 1, 4);
            drawTableCell(records.get(i).getAvgSpeed(), 13);
            drawTableCell(records.get(i).getMaxSpeed(), 12);
            drawTableCell(records.get(i).getAvgFuel(), 12);
            drawTableCell(records.get(i).getMaxFuel(), 11);
            drawTableCell(records.get(i).getJourneyDistance(), 16);
            drawTableCell(records.get(i).getJourneyTime(), 15);
            drawTableCell(records.get(i).getCounter(), 10);
            drawTableCell(records.get(i).getDayCounter1(), 16);
            drawTableCell(records.get(i).getDayCounter2(), 16);
            drawTableCell(records.get(i).getCreateDate(), 13);
            purpleTextColor.print("   |\n");
        }
        purpleTextColor.println("\t|" + " ".repeat(142) + "|");
        purpleTextColor.println("\t" + "=".repeat(144));
        yellowTextColor.println("\n\tOptions:");
        blueTextColor.print("\t  0) ");
        cyanTextColor.println("Cancel \n");
        yellowTextColor.print("\tChoose record: ");
        Scanner scanner = new Scanner(System.in);
        int choice;
        try {
            choice = Short.parseShort(scanner.next());
        } catch (NumberFormatException e) {
            choice = -1;
        }
        if (choice == 0)
            drawImportMenu();
        else if (choice < 0 || choice > records.size()) {
            redTextColor.println("\n\tWrong choice!");
            waitForEnter(false);
            chooseDBRecord();
        }
        return records.get(choice - 1);
    }

    private void drawTableCell(Object cell, int columnWidth) {
        StringBuilder result = new StringBuilder();
        String repeat = " ".repeat(Math.max(0, columnWidth - cell.toString().length()));
        result.append(repeat);
        result.append(cell.toString());

        cyanTextColor.print(result);
    }

    @Override
    public void reloadAfterSettings() {
        super.reloadAfterSettings();
    }

    @Override
    public void startStopEngine() {
        if (engineRunning) {
            KeyListener(true);
            speedThread = new SpeedThread(this, 0);
            speedThread.setEngineRunning(true);
            speedThread.start();
        } else {
//            keyListen = false;
            keyListen.set(false);
            KeyListener(false);
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

    private static class CLS {
        /**
         * The entry point of application.
         *
         * @param arg the input arguments
         * @throws IOException          the io exception
         * @throws InterruptedException the interrupted exception
         */
        public static void main(String... arg) throws IOException, InterruptedException {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }
}
