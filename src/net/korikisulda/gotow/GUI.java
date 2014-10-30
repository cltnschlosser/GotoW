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
    public javax.swing.JTextArea output;
    private GotoW main;

    private javax.swing.JButton find;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton sfile;
    private javax.swing.JTextField tofind;

    public GUI(final GotoW main) {
        setTitle("Goto_w Finder GUI");
        this.main = main;
        final JFileChooser fc = new JFileChooser();
        initComponents();

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        }


        sfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
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
                                main.command(new String[]{tofind.getText()});
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

        sfile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        output = new javax.swing.JTextArea();
        find = new javax.swing.JButton();
        tofind = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sfile.setText("Select file");
        sfile.setToolTipText("");

        output.setColumns(20);
        output.setRows(5);
        jScrollPane1.setViewportView(output);

        find.setText("Find Goto_w");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(sfile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tofind, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(find))
                        .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(sfile)
                                        .addComponent(find)
                                        .addComponent(tofind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>
}
