package org.Presentation;


import org.Logic.Dashboard;
import org.Logic.GearException;
import org.Logic.SpeedThread;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class TUI extends UIController {
    Thread t;
    public static  void main(String[] args) {
//        Runtime rt = Runtime.getRuntime();
//        try {
//            //rt.exec(new String[]{"cmd.exe","/c","start"});
//            rt.exec(new String[]{"cmd.exe","/c","start","java TUI"});
////            Process p = Runtime.getRuntime().exec("java TUI");
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        Console console = System.console();

        if(console == null)
            System.out.println("Wrong console! You need to execute me in a system console, please");
        else {
            TUI tui = new TUI();
            tui.processing();

            SpeedThread speedThread = new SpeedThread(tui, 0);
            speedThread.setEngineRunning(true);
            speedThread.start();
        }
    }

    public void processing()
    {
        t=new Thread(){
            public void run()
            {
                int znak;
                Scanner scanner = new Scanner(System.in);
                for(int i=0;i<=100;i++)
                {
                    znak = scanner.nextInt();
                    if(znak >= 0 && znak <=6) {
                        try {
                            dashboard.setCurrentGear((short) znak,true);
                        } catch (GearException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(znak == 7) {
                        dashboard.setKeyUp(true);
                    }
                    else if(znak == 8) {
                        dashboard.setKeyUp(false);
                    }
                    try{
                        t.sleep(100);
                    } catch(Exception e){}
                }
            }
        };
        t.start();
    }

    private static class CLS {
        public static void main(String... arg) throws IOException, InterruptedException {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }

    @Override
    public Dashboard getDashboard() {
        return dashboard;
    }

    @Override
    public void refresh() {
        try {
            CLS.main();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print("=====================================\n");
        System.out.print("| " + "16:25" + "   " + "Cruise control: Off     "+ dashboard.getCurrentGear() +" |\n");
        System.out.print("-------------------------------------\n");
        System.out.print("| avg. speed:" + dashboard.getOnBoardComputer().getAvgSpeed() + "     avg. fuel"+dashboard.getOnBoardComputer().getAvgCombustion()+" |\n");
        System.out.print("-------------------------------------\n");
        System.out.print("| speed:       " + dashboard.getSpeed() + "       revs: "+ dashboard.getRevs() +" |\n");

    }

    @Override
    public void reloadAfterSettings() {
        super.reloadAfterSettings();
    }

    @Override
    public void startStopEngine() {

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
