package life;

import life.controller.Input;
import life.model.Universe;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread mainThred = new Thread(new mainThred());
        mainThred.start();
    }
    static boolean disable = true;
    public void mainThredClose() {
        try{
            Thread.sleep(1100);
            disable = false;
            Thread.sleep(1000);
        }
        catch(InterruptedException e){
            System.out.println("Thread has been interrupted");
        }
    }


}
class mainThred implements Runnable {
    @Override
    public void run() {
        while(Main.disable){
            try {
                Input.executeInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Universe universe = new Universe();
            new Universe();
            try {
                universe.executeMethodsUniverse();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
