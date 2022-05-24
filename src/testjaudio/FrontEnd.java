/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testjaudio;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static testjaudio.TestJAudio.acCtrl;

/**
 *
 * @author admin
 */
public class FrontEnd {

    public JFrame f;
    JPanel p;
    JButton button1 = new JButton("OPEN FILE");
    JButton button2 = new JButton("CLASSIFY");
    JButton btnPlay = new JButton("PLAY");
    JButton btnPause = new JButton("PAUSE");
    filechooser f1 = new filechooser();
    public JList<String> list = null;
    JScrollPane scrollPane = new JScrollPane();
    static JLabel l = new JLabel("0 file đã chọn.");
    public JLabel lblLength = new JLabel("Độ dài file: 0 giây.");
    JLabel lblChoose = new JLabel("Hãy chọn 30s để classify.");
    JLabel lblStartMin = new JLabel("Phút bắt đầu");
    JLabel lblEndMin = new JLabel("Giây bắt đầu");
    JLabel lblStartSec = new JLabel("Phút kết thúc");
    JLabel lblEndSec = new JLabel("Giây kết thúc");
    public JLabel lblTime = new JLabel("00:00");
    public JSlider slider = new JSlider(0, 1000000, 0);
    public JTextField tfStartMin = new JTextField("0",3);
    public JTextField tfEndMin = new JTextField("20",3);
    public JTextField tfStartSec = new JTextField("0",3);
    public JTextField tfEndSec = new JTextField("50",3);

    public void PrepareGUI() {
        // frame to contains GUI elements
        f = new JFrame("Music Genre Classifier");
        //f.setLayout(new GridLayout(2, 3));
        f.setLayout(new BorderLayout());
        f.setSize(600, 600);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button1.addActionListener(f1);
        button2.addActionListener(f1);
        btnPlay.addActionListener(f1);
        btnPause.addActionListener(f1);
        slider.addChangeListener((ChangeEvent e) -> {
            long val = slider.getValue();
            acCtrl.SetCurrTime(val);
            lblTime.setText(acCtrl.ConvertTime());
        });

        List<JComponent> components = new ArrayList<>();
        components.add(button1);
        components.add(btnPlay);
        components.add(lblTime);
        components.add(lblLength);
        components.add(lblStartMin);
        components.add(lblEndMin);
        components.add(lblStartSec);
        components.add(lblEndSec);
        components.add(l);
        components.add(button2);
        components.add(btnPause);
        components.add(slider);
        components.add(lblChoose);
        components.add(tfStartMin);
        components.add(tfEndMin);
        components.add(tfStartSec);
        components.add(tfEndSec);
        components.add(scrollPane);

        // make a panel to add the buttons and labels
        p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        int k = 0;
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < components.size()/2 +1; j++) {
                c.gridx = i;
                c.gridy = j;
                p.add(components.get(k), c);
                k++;
            }
        }
        
        f.add(p, BorderLayout.CENTER);
        f.setVisible(true);
    }

    public void changeFileList(List<String> strs) {
        list = new JList<>(strs.toArray(new String[strs.size()]));
        scrollPane.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);
    }
}
