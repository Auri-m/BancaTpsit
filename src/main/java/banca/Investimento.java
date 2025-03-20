package banca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Investimento extends JFrame {

  private String durataScelta;
  private String rischioScelto;
  private double importoInserito = 0;
  private String risultato = "";
  private int tempo;

  public Investimento(String filePath, String pathGrafico) {
    setTitle("Investimento");
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

    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    String ultimaRiga = FileTools.leggiUltimariga(filePath);

    String[] dati = ultimaRiga.split(";");
    double portafoglio = Double.parseDouble(dati[2]);
    double contoCorrente = Double.parseDouble(dati[1]);
    int settimana = Integer.parseInt(dati[0]);

    JLabel contoCorrenteLabel = new JLabel("Nel conto hai " + contoCorrente);

    JLabel durataLabel = new JLabel("Durata:");
    String[] durate = {"-", "breve", "media", "lunga"};
    JComboBox<String> durataComboBox = new JComboBox<>(durate);

    JLabel rischioLabel = new JLabel("Rischio/Guadagno:");
    String[] rischi = {"-", "basso", "medio", "alto"};
    JComboBox<String> rischioComboBox = new JComboBox<>(rischi);

    JLabel importoLabel = new JLabel("Importo:");
    JTextField importoField = new JTextField(15);

    JButton confermaButton = new JButton("Conferma");

    durataComboBox.addActionListener(e -> durataScelta = (String) durataComboBox.getSelectedItem());

    rischioComboBox.addActionListener(
        e -> rischioScelto = (String) rischioComboBox.getSelectedItem());

    gbc.insets = new Insets(10, 10, 10, 10);

    gbc.gridx = 1;
    gbc.gridy = 0;
    add(contoCorrenteLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    add(durataLabel, gbc);

    gbc.gridx = 1;
    add(durataComboBox, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    add(rischioLabel, gbc);

    gbc.gridx = 1;
    add(rischioComboBox, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    add(importoLabel, gbc);

    gbc.gridx = 1;
    add(importoField, gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    add(confermaButton, gbc);

    confermaButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String importoInseritoString = importoField.getText().trim();
            action(
                filePath,
                pathGrafico,
                importoInseritoString,
                settimana,
                portafoglio,
                contoCorrente);
          }
        });

    setVisible(true);
  }

  /**
   * Metodo che racchiude tutte le azioni da svolgere dopo che l'utente ha schiacciato su "Conferma"
   *
   * @param filePath -- Percorso del file interessato
   * @param pathGrafico -- Percorso per il file dedicato al grafico
   * @param importoInseritoString -- Variabile che contiene la stringa inserita dall'utente nel
   *     campo importoField
   * @param settimana -- Settimana attuale dell'operazione
   * @param portafoglio -- Valore portafoglio attualmente (prima delle operazioni)
   * @param contoCorrente -- Valore conto corrente attualmente (prima delle operazioni)
   */
  private void action(
      String filePath,
      String pathGrafico,
      String importoInseritoString,
      int settimana,
      double portafoglio,
      double contoCorrente) {
    importoInserito = 0;
    boolean giusto = true;

    try {
      importoInserito = Double.parseDouble(importoInseritoString);
    } catch (NumberFormatException e1) {
      giusto = false;
    }

    if (!giusto || (importoInserito <= 0 || importoInserito > contoCorrente)) {
      JOptionPane.showMessageDialog(
          null, "Non puoi inserire queste valore!", "Errore", JOptionPane.ERROR_MESSAGE);
    } else {
      String messaggioRecap =
          "Hai scelto:\n"
              + "Periodo: "
              + durataScelta
              + "\n"
              + "Rischio: "
              + rischioScelto
              + "\n"
              + "Importo: "
              + importoInserito;

      double[] dati = investimento(rischioScelto, contoCorrente, importoInserito);
      double soldiVintiPersi = dati[0];
      double nuovoConto = dati[1];

      String messaggioRisultato =
          risultato
              + "\n"
              + "Risultato: "
              + soldiVintiPersi
              + "\n"
              + "Conto attuale: "
              + nuovoConto;

      JOptionPane.showMessageDialog(null, messaggioRecap);

      JOptionPane.showMessageDialog(null, messaggioRisultato);

      avanzamento(filePath, pathGrafico, settimana, portafoglio, nuovoConto);

      setVisible(false);
    }
  }

  /**
   * Metodo che in base al tipo di rischio/guadagno ("basso", "medio", "alto") richiama il metodo
   * per calcolare l'investimento. Ogni tipo di rischio/guadagno ha diverse probabilita' di vincita
   *
   * @param rischioGuadagnoScelto -- Stringa che contiene il tipo di rischio/guadagno scelto
   * @param contoCorrente -- Valore che contiene il conto corrente attuale
   * @param importoInserito -- Valore che l'utente vuole investire
   * @return restituisce un vettore di double nel quale la posizione 0 contiene il valore che indica
   *     quantti soldi ha vinto/perso e la posizione 1 contiene il nuovo valore del conto in banca
   */
  public double[] investimento(
      String rischioGuadagnoScelto, double contoCorrente, double importoInserito) {
    double[] dati = {0, 0};

    switch (rischioGuadagnoScelto) {
      case "basso":
        {
          dati = calcInvestimento(8, 25, 1, contoCorrente, importoInserito);
          break;
        }

      case "medio":
        {
          dati = calcInvestimento(5, 50, 15, contoCorrente, importoInserito);
          break;
        }

      case "alto":
        {
          dati = calcInvestimento(2, 100, 40, contoCorrente, importoInserito);
          break;
        }
    }

    return dati;
  }

  /**
   * Metodo che calcola il risultato dell'investimento
   *
   * @param probabilita -- Valore che indica il numero massimo per poter vincere
   * @param max -- Valore che indica la percentuale massima di vincita
   * @param min -- Valore che indica la percentuale minima di vincita
   * @param contoCorrente -- Valore che indica il conto corrente attuale
   * @param importoInserito -- Valore che l'utente vuole investire
   * @return restituisce un vettore di double in cui nella posizione 0 e' presente il valore che
   *     indica quantti soldi ha vinto/perso e nella posizione 1 e' presente il nuovo valore del
   *     conto in banca
   */
  private double[] calcInvestimento(
      int probabilita, int max, int min, double contoCorrente, double importoInserito) {
    int NGenerato = (int) FileTools.GeneraRandom(10, 1);
    double[] dati = {0, 0}; // dati[0] = soldi vinti o persi, dati[1] = nuovo conto corrente

    if (NGenerato <= probabilita) {
      risultato = "Hai guadagnato!";

      dati[0] = ((importoInserito / 100) * FileTools.GeneraRandom(max, min)) * 100;
      dati[0] = Math.floor(dati[0] * 100) / 100;
      dati[1] = contoCorrente + dati[0];
    } else {
      risultato = "NON hai guadagnato!";
      dati[0] = -importoInserito;
      dati[1] = contoCorrente - importoInserito;
    }

    return dati;
  }

  /**
   * Metodo che crea un'interfaccia GUI per l'avanzamento nel tempo
   *
   * @param filePath -- Percorso del file interessato
   * @param pathGrafico -- Percorso per il file dedicato al grafico
   * @param settimana -- Settimana attuale
   * @param portafoglio -- Variabile che indica il portafoglio attuale
   * @param nuovoConto -- Variabile che indica il conto corrente attuale
   */
  private void avanzamento(
      String filePath, String pathGrafico, int settimana, double portafoglio, double nuovoConto) {
    Tempo avanzamento = new Tempo(filePath, pathGrafico);
    avanzamento.setVisible(true);

    double conto = nuovoConto;

    avanzamento.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            tempo = settimana + avanzamento.getWeek();
            try {
              FileTools.aggiungiNuovaRiga(filePath, portafoglio, conto, tempo, "Investimento");
            } catch (IOException e1) {
              e1.printStackTrace();
            }
            dispose();
          }
        });
  }

  /**
   * Metodo per prendere le settimane attuali
   *
   * @return settimane attuali
   */
  public int getTempo() {
    return tempo;
  }

  /**
   * Metodo che restituisce l'importo inserito nel campo importoField
   *
   * @return l'importo inserito nel campo importoField
   */
  public double getImportoInserito() {
    return importoInserito;
  }
}
