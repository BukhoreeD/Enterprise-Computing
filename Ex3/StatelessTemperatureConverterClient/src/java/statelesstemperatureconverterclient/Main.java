/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statelesstemperatureconverterclient;

import java.util.Scanner;

/**
 *
 * @author Bukhoree
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter degree in Fahrenheit: ");
        float fahrenheit = scanner.nextFloat();
        Thread thread = new MyThread(fahrenheit);
//        MyThread myThread1 = new MyThread(1);
//        MyThread myThread2 = new MyThread(2);
//        MyThread myThread3 = new MyThread(3);
//        MyThread myThread4 = new MyThread(4);
        thread.start();
//        myThread1.start();
//        myThread2.start();
//        myThread3.start();
//        myThread4.start();
        try {
            thread.join();
//            myThread1.join();
//            myThread2.join();
//            myThread3.join();
//            myThread4.join();
        } catch (Exception e) {

        }
    }

}
