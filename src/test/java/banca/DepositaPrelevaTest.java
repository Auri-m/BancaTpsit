package banca;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DepositaPrelevaTest {

  @Test
  void test() {
    // fai due test separati
    String azione = "Deposita"; // "Deposita" o "Preleva"
    String filePath = "C:\\Users\\Utente\\Documents\\AURORA\\BancaTpsit\\prova.csv";
    DepositaPreleva prova = new DepositaPreleva(filePath, azione);

    double conto = 120, portafoglio = 20, soldiPrelevareDepositare = 12;
    double[] dati;
    if (azione.equals("Deposita")) {
      dati = prova.depositoPrelievo(null, true, portafoglio, conto, soldiPrelevareDepositare);
    } else {
      dati = prova.depositoPrelievo(null, true, conto, portafoglio, soldiPrelevareDepositare);
    }

    portafoglio = dati[0];
    conto = dati[1];

    boolean risultato = !(portafoglio < 0) && !(conto < 0);

    Assert.assertTrue(risultato);
    prova.dispose();
  }
}
