package banca;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;

public class Storico extends JFrame {

  private final JTextArea textArea;

  public Storico(String filePath) {
    setTitle("Storico Transazioni");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    JButton confirmButton = new JButton("Letto");
    confirmButton.setPreferredSize(new Dimension(590, 25));
    confirmButton.setHorizontalAlignment(SwingConstants.CENTER);
    contentPanel.add(confirmButton, BorderLayout.SOUTH);

    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);

    // Carica le transazioni dal file
    caricaStorico(filePath);

    JScrollPane actionLogScroll = new JScrollPane(textArea);
    contentPanel.add(actionLogScroll, BorderLayout.CENTER);

    add(contentPanel, BorderLayout.CENTER);

    confirmButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dispose();
          }
        });
  }

  /**
   * Metodo per caricare lo storico riguardante un file di un utente della banca
   *
   * @param filePath -- Percorso del file interessato
   */
  private void caricaStorico(String filePath) {
    try {
      ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));

      for (int i = 1; i < lines.size(); i++) {
        if (!lines.get(i).isEmpty()) {
          String[] dati = lines.get(i).split(";");
          String portafoglio = dati[2],
              contoCorrente = dati[1],
              settimana = dati[0],
              azione = dati[3];
          textArea.append(
              "Settimana "
                  + settimana
                  + ", "
                  + azione
                  + ", conto corrente: "
                  + contoCorrente
                  + ", portafoglio: "
                  + portafoglio
                  + "\n");
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
          this, "Errore nella lettura del file.", "Errore", JOptionPane.ERROR_MESSAGE);
    }
  }
}
