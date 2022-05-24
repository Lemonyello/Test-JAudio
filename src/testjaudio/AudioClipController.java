/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testjaudio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import static testjaudio.TestJAudio.acCtrl;
import static testjaudio.TestJAudio.fe;
/**
 *
 * @author admin
 */
public class AudioClipController {
    
    Clip clip;
    long clipTime;

    public void PlayClip(File clipFile) throws IOException, 
      UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
      try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile)) {
        Clip newClip = AudioSystem.getClip();
        if(IsRunning())
            return;
        
        if(HasClip()) {
           PlayClip();
            return;
        }
        
        this.clip = newClip;
        newClip.open(audioInputStream);
          SetCurrTime(1000000);
        fe.lblLength.setText("Độ dài file: " + ConvertTime() + " phút.");
          SetCurrTime(0);
        try {
            Timer timer = new Timer();
            newClip.start();
            timer.schedule(new UpdateTime(), 0, 1000);
        } finally {
        }
      }
    }
    
    public void PlayClip() {
        clip.setMicrosecondPosition(clipTime);
        clip.start();
    }
    
    public void PauseClip() {
        clipTime= clip.getMicrosecondPosition();
        clip.stop();
    }
    
    class UpdateTime extends TimerTask {
        public void run() {
            fe.lblTime.setText(ConvertTime());
            fe.slider.setValue((int) (GetElapsedPercent() * 1000000));
        }
    }
    
    public boolean IsLengthQualified(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file); 
        Clip c = AudioSystem.getClip();
        c.open(audioInputStream);
        return ( (c.getMicrosecondLength()/1000000) <= 10 * 60);
    }
    
    Boolean IsRunning() {
        return (clip != null && clip.isRunning());
    }
    
    public Boolean HasClip() {
        return (clip != null && clip.isOpen());
    }
    
    public long GetCurrTime() {
        return clip.getMicrosecondPosition();
    }
    
    public void SetCurrTime(long val) {
        clip.setMicrosecondPosition((clip.getMicrosecondLength()* val/1000000));
    }
    
    double GetElapsedPercent() {
        double a = (double)(clip.getMicrosecondPosition())/clip.getMicrosecondLength();
        return Math.round(a * 1000000.0) / 1000000.0;
    }
    
    String ConvertTime() {
        long curSec = TimeUnit.MILLISECONDS.toSeconds(acCtrl.GetCurrTime() / 1000);
        long def = 60;
        long min = curSec/def;
        long sec = curSec%def;
        String minStr = String.valueOf(min);
        String secStr = String.valueOf(sec);
        if(min<10) minStr = "0" + String.valueOf(min);
        if(sec<10) secStr = "0" + String.valueOf(sec);
        return (minStr + ":" + secStr);
    }
}
