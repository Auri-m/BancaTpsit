package banca;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Tempo extends JFrame {
  private int week;

  public Tempo(String filePath, String pathGrafico) {
    setTitle("DEUTCH BANK");
    setSize(400, 300);
    addWindowListener(
        new WindowAdapter() {
          @SuppressWarnings("unused")
          public void WindowClosing(WindowEvent e) {
            FileTools.esciEntra(filePath, 0);
            dispose();
          }
        });
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(null);
    add(panel);

    String ultimaRiga = FileTools.leggiUltimariga(filePath);

    String[] dati = ultimaRiga.split(";");
    double portafoglio = Double.parseDouble(dati[2]);
    double contoCorrente = Double.parseDouble(dati[1]);
    int settimana = Integer.parseInt(dati[0]);

    JLabel settimanaLabel = new JLabel("Sei alla " + settimana + " settimana");
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            int labelWidth = settimanaLabel.getPreferredSize().width;

            int x = (400 - labelWidth) / 2;

            int y = 60;

            settimanaLabel.setBounds(x, y, labelWidth, 25);
          }
        });
    panel.add(settimanaLabel);

    JLabel dateLabel = new JLabel("Avanzamento:");
    dateLabel.setBounds(50, 100, 100, 25);
    panel.add(dateLabel);

    JTextField dateField = new JTextField();
    dateField.setBounds(150, 100, 200, 25);
    panel.add(dateField);

    JButton confirmButton = new JButton("Conferma");
    confirmButton.setBounds(150, 150, 100, 25);
    panel.add(confirmButton);

    confirmButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String mesiString = dateField.getText().trim();
            action(filePath, pathGrafico, panel, mesiString, settimana, portafoglio, contoCorrente);
          }
        });
  }

  /**
   * Metodo che racchiude tutti i controlli collegati al botton "Conferma"
   *
   * @param filePath -- Percorso del file interessato
   * @param pathGrafico -- Percorso per il file dedicato al grafico
   * @param panel -- JPanel al quale si collega la finestra per mandare messaggi
   * @param mesiString -- Variabile che contiene la stringa inserita dall'utente nel campo dateField
   * @param settimana -- Settimana attuale dell'operazione
   * @param portafoglio -- Valore portafoglio attualmente
   * @param contoCorrente -- Valore conto corrente attualmente
   */
  private void action(
      String filePath,
      String pathGrafico,
      JPanel panel,
      String mesiString,
      int settimana,
      double portafoglio,
      double contoCorrente) {
    week = 0;
    boolean giusto = true;

    try {
      week = Integer.parseInt(mesiString);
    } catch (NumberFormatException e1) {
      giusto = false;
    }

    if (giusto && week <= 0) {
      JOptionPane.showMessageDialog(panel, "Dato errato", "Errore", JOptionPane.ERROR_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(panel, "Avanzamento svolto");

      int tempo = settimana + week;
      portafoglio += (100 * week);

      try {
        FileTools.aggiungiNuovaRiga(filePath, portafoglio, contoCorrente, tempo, "Avanzamento");
        FileTools.aggiungiFileGrafico(pathGrafico, portafoglio, contoCorrente, settimana);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      dispose();
    }
  }

  /**
   * Metodo per prendere le settimane attuali
   *
   * @return settimane attuali
   */
  public int getWeek() {
    return week;
  }
}
