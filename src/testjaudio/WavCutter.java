/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testjaudio;

/**
 *
 * @author admin
 */

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
/**
 * wav Audio file interception tool 
 *  (Applicable to the bit rate of 128kbps Adj. wav Audio files, the length of header information of such audio files 44 Bytes) 
 * @author lwj
 *
 */
public class WavCutter {
  /**
   *  Interception wav Audio file 
     * @param sourcefile
     * @param targetfile 
   * @param start  Intercept start time (seconds) 
   * @param end  Intercept end time (seconds) 
   *
   * return  Successful interception returns true Otherwise, return false
     * @return 
   */
  public boolean cut(String sourcefile, String targetfile, int start, int end) {
    try{
      if(!sourcefile.toLowerCase().endsWith(".wav") || !targetfile.toLowerCase().endsWith(".wav")){
        return false;
      }
      File wav = new File(sourcefile);
      if(!wav.exists()){
        return false;
      }
      long t1 = getTimeLen(wav); // Total duration ( Seconds )
      if(start<0 || end<=0 || start>=t1 || end>t1 || start>=end){
        return false;
      }
      FileInputStream fis = new FileInputStream(wav);
      long wavSize = wav.length()-44; // Audio data size ( 44 For 128kbps Bit rate wav File header length) 
      long splitSize = (wavSize/t1)*(end-start); // Size of Intercepted Audio Data 
      long skipSize = (wavSize/t1)*start; // Size of audio data skipped during interception 
      int splitSizeInt = Integer.parseInt(String.valueOf(splitSize));
      int skipSizeInt = Integer.parseInt(String.valueOf(skipSize));
      ByteBuffer buf1 = ByteBuffer.allocate(4); // Store file size ,4 Representative 1 A int Number of bytes occupied 
      buf1.putInt(splitSizeInt+36); // Put in file length information 
      byte[] flen = buf1.array(); // Represents file length 
      ByteBuffer buf2 = ByteBuffer.allocate(4); // Store audio data size, 4 Representative 1 A int Number of bytes occupied 
      buf2.putInt(splitSizeInt); // Input data length information 
      byte[] dlen = buf2.array(); // Represents data length 
      flen = reverse(flen); // Array inversion 
      dlen = reverse(dlen);
      byte[] head = new byte[44]; // Definition wav Header information array 
      fis.read(head, 0, head.length); // Read source wav File header information 
      for(int i=0; i<4; i++){ //4 Representative 1 A int Number of bytes occupied 
        head[i+4] = flen[i]; // Replace the file length in the original header information 
        head[i+40] = dlen[i]; // Replace the data length in the original header information 
      }
      byte[] fbyte = new byte[splitSizeInt+head.length]; // Store the intercepted audio data 
      for(int i=0; i<head.length; i++){ // Put in the modified header information 
        fbyte[i] = head[i];
      }
      byte[] skipBytes = new byte[skipSizeInt]; // Store audio data skipped during interception 
      fis.read(skipBytes, 0, skipBytes.length); // Skip data that does not need to be intercepted 
      fis.read(fbyte, head.length, fbyte.length-head.length); // Read the data to be intercepted into the target array 
      fis.close();
      File target = new File(targetfile);
      if(target.exists()){ // Delete the destination file if it already exists 
        target.delete();
      }
      FileOutputStream fos = new FileOutputStream(target);
      fos.write(fbyte);
      fos.flush();
      fos.close();
    }catch(IOException e){
      return false;
    }
    return true;
  }
  /**
   *  Get the total duration of audio files 
     * @param file
   * @return
   */
      public static long getTimeLen(File file){
        long tlen = 0;
        if(file!=null && file.exists()){
          Encoder encoder = new Encoder();
          try {
             MultimediaInfo m = encoder.getInfo(file);
             long ls = m.getDuration();
             tlen = ls/1000;
          } catch (EncoderException e) {
          }
        }
        return tlen;
      }
  /**
  *  Array inversion 
  * @param array
     * @return 
  */
      public static byte[] reverse(byte[] array){
        byte temp;
        int len=array.length;
        for(int i=0;i<len/2;i++){
          temp=array[i];
          array[i]=array[len-1-i];
          array[len-1-i]=temp;
        }
        return array;
      }
     
     public void ConvertAudio(String sourcef, String targetf) {
         Encoder encoder = new Encoder();
        EncodingAttributes attributes = new EncodingAttributes();
        attributes.setFormat("wav");
        AudioAttributes audio = new AudioAttributes();
        audio.setBitRate(64000);
        audio.setChannels(1);
        audio.setSamplingRate(22050);
        attributes.setAudioAttributes(audio);

        File source = new File(sourcef);
        File target = new File(targetf);
        try {
            encoder.encode(source, target, attributes);
        }catch (IllegalArgumentException | EncoderException e1) {
             // TODO Auto-generated catch block
        }
     }
}


