/*
 * MainWindow.java
 *
 * @author Giuliano Vilela
 */

package graphxt.gui;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MainWindow
 * 
 * Classe que modela a janela principal
 * do programa.
 * 
 * @author Giuliano Vilela
 */
public class MainWindow extends javax.swing.JFrame {
    
    /**
     * Cria uma nova MainWindow com os seus componentes
     * devidamente inicializados.
     */
    public MainWindow() {
        initComponents();
        windows = new LinkedList<GraphWindow>();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    desktopPane = new javax.swing.JDesktopPane();
    jToolBar1 = new javax.swing.JToolBar();
    jButton3 = new javax.swing.JButton();
    jSeparator4 = new javax.swing.JToolBar.Separator();
    jButton1 = new javax.swing.JButton();
    jSeparator3 = new javax.swing.JToolBar.Separator();
    jButton2 = new javax.swing.JButton();
    menuBar = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    openMenuItem = new javax.swing.JMenuItem();
    saveMenuItem = new javax.swing.JMenuItem();
    saveAsMenuItem = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JSeparator();
    exitMenuItem = new javax.swing.JMenuItem();
    helpMenu = new javax.swing.JMenu();
    aboutMenuItem = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("GraphXT");
    setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
    setMinimumSize(new java.awt.Dimension(1024, 720));

    desktopPane.setBackground(new java.awt.Color(50, 50, 50));
    desktopPane.setDoubleBuffered(true);

    jToolBar1.setRollover(true);
    jToolBar1.setMaximumSize(new java.awt.Dimension(130, 41));

    jButton3.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphxt/images/folder-new.png"))); // NOI18N
    jButton3.setFocusable(false);
    jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton3.setIconTextGap(0);
    jButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton3ActionPerformed(evt);
      }
    });
    jToolBar1.add(jButton3);
    jToolBar1.add(jSeparator4);

    jButton1.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphxt/images/document-open.png"))); // NOI18N
    jButton1.setFocusable(false);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
    jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    jToolBar1.add(jButton1);
    jToolBar1.add(jSeparator3);

    jButton2.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphxt/images/application-exit.png"))); // NOI18N
    jButton2.setFocusable(false);
    jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
    jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });
    jToolBar1.add(jButton2);

    fileMenu.setText("File");
    fileMenu.setFont(new java.awt.Font("DejaVu Sans", 0, 11));

    openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
    openMenuItem.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    openMenuItem.setText("New");
    openMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openMenuItem);

    saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    saveMenuItem.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    saveMenuItem.setText("Open");
    saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(saveMenuItem);

    saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
    saveAsMenuItem.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    saveAsMenuItem.setText("Close all");
    saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAsMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(saveAsMenuItem);
    fileMenu.add(jSeparator1);

    exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
    exitMenuItem.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    exitMenuItem.setText("Exit");
    exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exitMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);

    helpMenu.setText("Help");
    helpMenu.setFont(new java.awt.Font("DejaVu Sans", 0, 11));

    aboutMenuItem.setFont(new java.awt.Font("DejaVu Sans", 0, 11));
    aboutMenuItem.setText("About");
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    setJMenuBar(menuBar);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
      .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Chamado à um evento de saída.
     * @param evt
     */
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed

    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * Gera uma nova GraphWindow e a posiciona
     * randômicamente na tela.
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        GraphWindow w = new GraphWindow();
        desktopPane.add(w);
        windows.add(w);
        w.setLocation(
            (int)(Math.random()*(desktopPane.getWidth()-w.getWidth())),
            (int)(Math.random()*(desktopPane.getHeight()-w.getHeight()))
        );
        w.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * Remove todas as GraphWindow presentes
     * na tela.
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        while (!windows.isEmpty()) {
            GraphWindow w = windows.pop();
            w.close();
            desktopPane.remove(w);
        }
        desktopPane.validate();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        jButton3ActionPerformed(evt);
    }//GEN-LAST:event_openMenuItemActionPerformed

    /**
     * Cria um GraphWindow e abre uma janela para
     * inicializar um grafo à partir de um arquivo.
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            GraphWindow w = new GraphWindow();
            desktopPane.add(w);
            windows.add(w);
            w.setLocation((int) (Math.random()*(desktopPane.getWidth()-w.getWidth())), (int) (Math.random()*(desktopPane.getHeight()-w.getHeight())));
            w.setVisible(true);
            w.openFile();
        } catch (Exception ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }//GEN-LAST:event_jButton1ActionPerformed
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        jButton1ActionPerformed(null);
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        jButton2ActionPerformed(null);
    }//GEN-LAST:event_saveAsMenuItemActionPerformed
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem aboutMenuItem;
  private javax.swing.JDesktopPane desktopPane;
  private javax.swing.JMenuItem exitMenuItem;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JMenu helpMenu;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JButton jButton3;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JToolBar.Separator jSeparator3;
  private javax.swing.JToolBar.Separator jSeparator4;
  private javax.swing.JToolBar jToolBar1;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenuItem openMenuItem;
  private javax.swing.JMenuItem saveAsMenuItem;
  private javax.swing.JMenuItem saveMenuItem;
  // End of variables declaration//GEN-END:variables
    // </editor-fold>
    private LinkedList<GraphWindow> windows;
}
