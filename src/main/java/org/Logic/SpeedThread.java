package org.Logic;

public class SpeedThread extends Thread {
    // FPS
    private static int MAX_FPS;
    private boolean running;
    private Dashboard dashboard;

    //Konstruktor klasy
    public SpeedThread(Dashboard dashboard, int MAX_FPS) {
        super(); //Wywołanie konstruktora rodzica (klasy Thread) bez parametrów
        this.dashboard = dashboard;
        this.MAX_FPS = MAX_FPS;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0; //Zmienna do zliczania klatek
        long totalTime = 0; //Zmienna do zliczania całkowitego czasu
        long targetTime = 1000/MAX_FPS; //1s = 1000ms,  zatem w wyniku obliczenia otrzymamy
        // oczekiwany czas w ms rysownia jednej klatki gry

        while(running) {
            startTime = System.nanoTime(); //Zapisujemy czas rozpoczęcia rysownia klatki

           /* try { //Spróbuj wykonać poniższy kod
                // by nikt inny nie mógł rysować oprócz nas na niej oraz rozpocznamy edycję pikseli na naszej powierzchni.
                synchronized (surfaceHolder) { //Tylko jeden wątek może wykonać poniższy blok kodu jednocześnie.
                    this.gamePanel.update(); //Aktualizuje położenie postaci oraz przeszkód
                    this.gamePanel.draw(canvas); //Rysuje zaktualizowany przed chwilą widok gry (klatke)
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally { //Nawet jeśli próba nie powedzie się wykonaj poniższy kod
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas); //odblokowuje dostęp dla innych wątków
                        // do Canvas'u/powierzchni na której rysujemy naszą gre
                    } catch (Exception e) {e.printStackTrace(); }
                }
            }*/

            timeMillis = (System.nanoTime() - startTime)/1000000; //Obliczamy czas jaki zajeło rysowanie klatki
            //oraz konwertujemy czas z ns na ms
            waitTime = targetTime - timeMillis; //Obliczamy czas na jaki należy uśpić wątek by
            //osiągnąć oczekiwany czas rysowania klatki
            try {
                if(waitTime > 0) { //jeśli czas uśpienia jest dodatni, czyli czas w jakim wątek
                    // narysował klatke był mniejszy od oczekiwanego czasu rysowania
                    this.sleep(waitTime); //uśpij wątek na pozostały czas by osiągnąc oczekiwany czas rysowania
                }
            } catch (Exception e) {e.printStackTrace();}

            totalTime += System.nanoTime() - startTime; //dodajemy czas rysownia klatki do całkowitego czasu
            frameCount++; //zwiększamy numer klatki
            if(frameCount == MAX_FPS) {
                //averageFPS = 1000/((totalTime/frameCount)/1000000); //Obliczamy średnią liczbę klatek na sekunde
                frameCount = 0;
                totalTime = 0;
               // System.out.println(averageFPS);
            }
        }
    }
}
