package banca;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.*;

public class HomePage extends JFrame {
  private final JLabel userNameLabel;
  private final JLabel dateLabel;
  private final JTextArea actionLogArea;
  private final JPanel buttonPanel;
  private int settimana = 1;

  public HomePage(String filePath, String pathGrafico, String nome) {
    setTitle("DEUTCH BANK");
    setSize(960, 540);
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            FileTools.esciEntra(filePath, 0);
            System.exit(0);
          }
        });
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    JPanel parteSopra = new JPanel(new BorderLayout());
    parteSopra.setLayout(new BorderLayout());

    String ultimaRiga = FileTools.leggiUltimariga(filePath);
    String[] dati = ultimaRiga.split(";");
    settimana = Integer.parseInt(dati[0]);

    dateLabel = new JLabel("Settimana " + settimana);
    dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
    parteSopra.add(dateLabel, BorderLayout.WEST);

    userNameLabel = new JLabel("Benvenuto " + nome);
    userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    parteSopra.add(userNameLabel, BorderLayout.EAST);
    contentPanel.add(parteSopra, BorderLayout.NORTH);

    actionLogArea = new JTextArea(3, 30);
    actionLogArea.setEditable(false);
    actionLogArea.setLineWrap(true);
    actionLogArea.setWrapStyleWord(true);
    JScrollPane actionLogScroll = new JScrollPane(actionLogArea);
    contentPanel.add(actionLogScroll, BorderLayout.CENTER);

    buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
    addButtons(filePath, pathGrafico);
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);

    add(contentPanel, BorderLayout.CENTER);

    setLocationRelativeTo(null);
  }

  /**
   * Metodo che inserisce i bottoni della parte bassa nella finestra
   *
   * @param filePath -- Percorso del file interessato
   * @param pathGrafico -- Percorso per il file dedicato al grafico
   */
  private void addButtons(String filePath, String pathGrafico) {
    String[] buttonLabels = {
      "Deposito", "Prelievo", "Investimento", "Avanti nel tempo", "Storico", "Esci"
    };

    for (String label : buttonLabels) {
      JButton button = new JButton(label);
      button.addActionListener(new ButtonClickListener(label, filePath, pathGrafico));
      buttonPanel.add(button);
    }
  }

  /**
   * Metodo che racchiude tutte le azioni collegate ai singoli bottoni
   *
   * @param filePath -- Percorso del file interessato
   * @param pathGrafico -- Percorso per il file dedicato al grafico
   * @param actionLabel -- Variabile che memorizza il testo del bottone schiacciato
   */
  private void actions(String filePath, String pathGrafico, String actionLabel) {
    if (controllo(filePath, actionLabel)) {
      setVisible(false);
      if (actionLabel.equals("Deposito")) {
        depositoPrelievo(filePath, "Deposita");
      }

      if (actionLabel.equals("Prelievo")) {
        depositoPrelievo(filePath, "Preleva");
      }

      if (actionLabel.equals("Investimento")) {
        investimento(filePath, pathGrafico);
      }

      if (actionLabel.equals("Avanti nel tempo")) {
        avanzamento(filePath, pathGrafico);
      }
      if (actionLabel.equals("Storico")) {
        storico(filePath);
      }

      if (actionLabel.equals("Esci")) {
        FileTools.esciEntra(filePath, 0);
        dispose();
      }

      actionLogArea.setCaretPosition(actionLogArea.getDocument().getLength());
    } else {
      JOptionPane.showMessageDialog(
          null, "Hai €0 nel conto oppure sei in rosso!", "Errore", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Metodo che controlla se il conto corrente e' <= 0 dopo aver schiacciato un tasto
   *
   * @param filePath -- Percorso del file interessato
   * @param actionLabel -- Variabile che memorizza il testo del bottone schiacciato
   * @return true nel caso in cui si possa fare l'azione indicata nell'actionLabel, false nel caso
   *     in cui il conto corrente sia <= 0 e l'actionLabel non sia "Deposito", "Avanti nel tempo",
   *     "Storico" o "Esci"
   */
  private boolean controllo(String filePath, String actionLabel) {
    String ultimaRiga = "";
    try {
      ultimaRiga =
          Files.readAllLines(Paths.get(filePath))
              .get(Files.readAllLines(Paths.get(filePath)).size() - 1);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String[] dati = ultimaRiga.split(";");
    double conto = Double.parseDouble(dati[1]);

    if (conto <= 0) {
      System.out.println("conto");
      return actionLabel.equals("Deposito")
          || actionLabel.equals("Avanti nel tempo")
          || actionLabel.equals("Storico")
          || actionLabel.equals("Esci");
    }
    return true;
  }

  /**
   * Metodo che crea un'interfaccia GUI per il bottone "Deposito" o "Prelievo" e stampa l'azione
   * sulla parte centrale della finestra
   *
   * @param filePath -- Percorso del file interessato
   * @param azione -- Variabile che memorizza la scelta ("Deposita" o "Preleva")
   */
  private void depositoPrelievo(String filePath, String azione) {
    DepositaPreleva depositoPrelievo = new DepositaPreleva(filePath, azione);
    depositoPrelievo.setVisible(true);

    depositoPrelievo.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            setVisible(true);
            if (azione.equals("Deposita")) {
              actionLogArea.append("Hai depositato " + depositoPrelievo.getSoldi() + " euro\n");
            } else {
              actionLogArea.append("Hai prelevato " + depositoPrelievo.getSoldi() + " euro\n");
            }
          }
        });
  }

  /**
   * Metodo che crea un'interfaccia GUI per il bottone "Investimento" e stampa l'azione sulla parte
   * centrale della finestra
   *
   * @param filePath -- Percorso del file interessato
   * @param pathGrafico -- Percorso per il file dedicato al grafico
   */
  private void investimento(String filePath, String pathGrafico) {
    Investimento investire = new Investimento(filePath, pathGrafico);
    investire.setVisible(true);

    investire.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            setVisible(true);
            settimana = investire.getTempo();
            dateLabel.setText("Settimana " + settimana);
            actionLogArea.append("Hai investito " + investire.getImportoInserito() + " euro\n");
          }
        });
  }

  /**
   * Metodo che crea un'interfaccia GUI per il bottone "Avanti nel tempo" e stampa l'azione sulla
   * parte centrale della finestra
   *
   * @param filePath -- Percorso del file interessato
   * @param pathGrafico -- Percorso per il file dedicato al grafico
   */
  private void avanzamento(String filePath, String pathGrafico) {
    Tempo avanzamento = new Tempo(filePath, pathGrafico);
    avanzamento.setVisible(true);

    avanzamento.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            setVisible(true);
            settimana += avanzamento.getWeek();
            dateLabel.setText("Settimana " + settimana);
            actionLogArea.append("Sei avanzato di " + avanzamento.getWeek() + " settimane\n");
          }
        });
  }

  /**
   * Metodo che crea un'interfaccia GUI per il bottone "Storico" e stampa l'azione sulla parte
   * centrale della finestra
   *
   * @param filePath -- Percorso del file interessato
   */
  private void storico(String filePath) {
    Storico storico = new Storico(filePath);
    storico.setVisible(true);

    storico.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent e) {
            setVisible(true);
            actionLogArea.append("Hai visualizzato lo storico\n");
          }
        });
  }

  private class ButtonClickListener implements ActionListener {
    private final String actionLabel;
    private final String filePath;
    private final String pathGrafico;

    public ButtonClickListener(String label, String filePath, String pathGrafico) {
      this.actionLabel = label;
      this.filePath = filePath;
      this.pathGrafico = pathGrafico;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      actions(filePath, pathGrafico, actionLabel);
    }
  }
}
