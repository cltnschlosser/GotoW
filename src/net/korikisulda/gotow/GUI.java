package net.korikisulda.gotow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Travis (Toyz) on 10/29/2014.
 */
public class GUI extends JFrame {
    private static final long serialVersionUID = 4259967289409131510L;
    public JTextArea output;

    private JButton find;
    private JScrollPane jScrollPane1;
    private JButton sfile;
    private JTextField tofind;

    public GUI() {
        setTitle("Goto_w Finder GUI");
        final JFileChooser fc = new JFileChooser();
        initComponents();

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e){}


        sfile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int returnVal = fc.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    tofind.setText("");
                    File file = fc.getSelectedFile();

                    tofind.setText(file.getAbsolutePath());
                }
            }
        });

        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tofind.getText().trim().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tofind.setEnabled(false);
                            find.setEnabled(false);
                            sfile.setEnabled(false);

                            output.setText("");
                            try {
                                GotoW.command(new String[]{tofind.getText()});
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                            tofind.setEnabled(true);
                            find.setEnabled(true);
                            sfile.setEnabled(true);
                        }
                    }).start();
                }
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        sfile = new JButton();
        jScrollPane1 = new JScrollPane();
        output = new JTextArea();
        find = new JButton();
        tofind = new JTextField();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        sfile.setText("Select file");
        sfile.setToolTipText("");

        output.setColumns(20);
        output.setRows(5);
        jScrollPane1.setViewportView(output);

        find.setText("Find Goto_w");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(sfile)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tofind, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(find))
                        .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(sfile)
                                        .addComponent(find)
                                        .addComponent(tofind, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>
}
