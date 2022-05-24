/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package testjaudio;


/**
 *
 * @author Admin
 */
// Java program to create open or
// save dialog using JFileChooser

public class TestJAudio {
    public static FrontEnd fe = new FrontEnd();
    public static ExtractFeatureController efc = new ExtractFeatureController();
    public static AudioClipController acCtrl = new AudioClipController();
    public static WavCutter wavCutter = new WavCutter();
    public static void main(String args[])
    {
        fe.PrepareGUI();
       //System.out.println(wavCutter.cut("F:\\Nothanks - Valium - Converted.wav", "file\\Nothanks - Valium - new.wav",50,70));
        //new WavCut().ConvertAudio("F:\\Nothanks - Valium.mp3", "F:\\Nothanks - Valium - Converted.wav");
    }
}



