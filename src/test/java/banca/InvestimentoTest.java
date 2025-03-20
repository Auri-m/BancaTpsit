package banca;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class InvestimentoTest {

	@Test
	void test() {
		String filePath = "C:\\Users\\Utente\\Documents\\AURORA\\BancaTpsit\\prova.csv";
		Investimento prova = new Investimento(filePath, filePath);

		String rischioScelto = "basso"; // "basso" o "medio" o "alto"
		double contoCorrente = 0, importoInserito = 10;

		boolean risultato = !(contoCorrente < importoInserito);

        double[] dati = prova.investimento(rischioScelto, contoCorrente, importoInserito);

		Assert.assertTrue(risultato);
	}

}
