package banca;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileTools {

  /**
   * Metodo che aggiorna il file di un utente della banca aggiungendo una nuova riga con i nuovi
   * dati passati come parametri
   *
   * @param filePath -- Percorso del file interessato
   * @param portafoglio -- Valore del nuovo portafoglio
   * @param contoCorrente -- Valore del nuovo conto corrente
   * @param settimana -- Valore della nuova settimana
   * @param azione -- Azione compiuta prima dell'aggiornamento
   * @throws IOException
   */
  public static void aggiungiNuovaRiga(
      String filePath, double portafoglio, double contoCorrente, int settimana, String azione)
      throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
      bw.newLine();
      bw.write(settimana + ";" + contoCorrente + ";" + portafoglio + ";" + azione);
    }
  }

  /**
   * Metodo per aggiornare i dati nel file corrispondente per la rappresentazione del grafico
   *
   * @param filePath -- Percorso del file interessato
   * @param portafoglio -- Valore del nuovo portafoglio
   * @param contoCorrente -- Valore del nuovo conto corrente
   * @param settimana -- Valore della nuova settimana
   * @throws IOException
   */
  public static void aggiungiFileGrafico(
      String filePath, double portafoglio, double contoCorrente, int settimana) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
      bw.newLine();
      bw.write(settimana + ";" + contoCorrente + ";" + portafoglio);
    }
  }

  /**
   * Metodo che legge l'ultima riga di un file
   *
   * @param filePath -- Percorso del file interessato
   */
  public static String leggiUltimariga(String filePath) {
    String ultimaRiga = "";
    try {
      ultimaRiga =
          Files.readAllLines(Paths.get(filePath))
              .get(Files.readAllLines(Paths.get(filePath)).size() - 1);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ultimaRiga;
  }

  /**
   * Metodo per indicare che un account si puo' aprire dopo la sua eventuale chiusura
   *
   * @param filePath -- Percorso del file interessato
   * @param flag -- Flag passato per indicare se si vuole indicare che un account sta andando in
   *     esecuzione(1) o se si sta chiudendo(0)
   */
  public static void esciEntra(String filePath, int flag) {
    try {
      Path path = Paths.get(filePath);
      ArrayList<String> righe = new ArrayList<String>(Files.readAllLines(path));
      String primaRiga = righe.get(0);

      String[] dati = primaRiga.split(";");
      primaRiga = dati[0] + ";" + flag;
      righe.set(0, primaRiga);

      Files.write(path, righe);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Metodo per generare un numero random, cosi da randomizzare alcuni eventi, come saldo iniziale e
   * le probabilita
   *
   * @param max -- Valore massimo del random
   * @param min -- Valore minimo del random
   * @return -- Ritorna un numero random compreso tra i valori richiesti prima
   */
  public static double GeneraRandom(double max, double min) {
    double random = Math.round((min + (Math.random() * max)) * 100) / 100;
    return random;
  }
}
