/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testjaudio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import static testjaudio.FrontEnd.l;
import static testjaudio.TestJAudio.efc;
import static testjaudio.TestJAudio.fe;
import static testjaudio.TestJAudio.acCtrl;
import static testjaudio.TestJAudio.wavCutter;

/**
 *
 * @author admin
 */
class filechooser extends JFrame implements ActionListener {
    
    File lastDir = new File(System.getProperty("user.dir"));
    File files[] = null;
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        // if the user presses the save button show the save dialog
        String com = evt.getActionCommand();

        switch (com) {
            case "OPEN FILE":
                // invoke the showsSaveDialog function to show the save dialog
                JFileChooser j = OpenFileChooser();
                int r = j.showSaveDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) 
                        LoadFiles(j);
                        // if the user cancelled the operation
                else {

                }
                break;
            case "CLASSIFY":
                if(files == null || fe.list.getSelectedIndex() == -1)
                    JOptionPane.showMessageDialog(fe.f, "Bạn phải chọn file audio để chạy.");
                else {
                    try {
                        ValidateFile();
                        efc.Classify();
                    } catch (Exception ex) {
                        Logger.getLogger(filechooser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "PLAY":
                if(fe.list == null || fe.list.getSelectedValuesList().size() > 1 || fe.list.getSelectedValuesList().isEmpty()){
                    JOptionPane.showMessageDialog(fe.f, "Bạn phải chọn 1 file audio để chạy.");
                } else {
                    try {
                        acCtrl.PlayClip(files[fe.list.getSelectedIndices()[0]]);
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
                        Logger.getLogger(filechooser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "PAUSE":
                if(acCtrl.HasClip())
                    acCtrl.PauseClip();
                else
                    JOptionPane.showMessageDialog(fe.f, "Bạn phải chọn 1 file audio để chạy.");
                break;
            default:
                throw new AssertionError();
        }
    }
    
    JFileChooser OpenFileChooser() {
    // create an object of JFileChooser class
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        j.setCurrentDirectory(lastDir);
        // resctrict the user to select files of all types
        j.setAcceptAllFileFilterUsed(false);
        // set a title for the dialog
        j.setDialogTitle("Select an audio file");
        // only allow files of .txt extension
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .wav .mp3 files", "wav", "mp3");
        j.addChoosableFileFilter(restrict);
        // allow multiple file selection
        j.setMultiSelectionEnabled(true);
        return j;
    }
    
    void LoadFiles(JFileChooser j){
        // get the selelcted files
        lastDir = j.getCurrentDirectory();
        files = j.getSelectedFiles();
        // set text to blank
        l.setText(files.length + " " + "file đã chọn.");
        // set the label to the path of the selected files
        List<String> myList = new ArrayList<>();
          for (File file: files) {
             myList.add(file.getName());
          }
          fe.changeFileList(myList);
    }
    
    void ValidateFile() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        int start = Integer.valueOf(fe.tfStartMin.getText()) * 60 + Integer.valueOf(fe.tfStartSec.getText());
        int end = Integer.valueOf(fe.tfEndMin.getText()) * 60 + Integer.valueOf(fe.tfEndSec.getText());

        int selected = fe.list.getSelectedIndex();
        if(files[selected].getName().endsWith(".mp3")) 
            wavCutter.ConvertAudio(files[selected].getAbsolutePath(), "file\\new_audio.wav");
        if(acCtrl.IsLengthQualified(files[selected])) {
            if(files[selected].getName().endsWith(".mp3"))
                wavCutter.cut("file\\new_audio.wav", "file\\cut_audio.wav", start, end);
            else {
                wavCutter.cut(files[selected].getAbsolutePath(), "file\\cut_audio.wav", start, end);
            }
            try {
                efc.MFCCjAudio(new File("file\\cut_audio.wav"));
            } catch (Exception ex) {
                Logger.getLogger(filechooser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
            JOptionPane.showMessageDialog(fe.f, "Hãy chọn file có độ dài ít hơn 10 phút.");
    }
}
