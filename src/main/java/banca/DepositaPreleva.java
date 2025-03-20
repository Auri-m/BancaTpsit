package banca;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class DepositaPreleva extends JFrame {
	private double soldi;
	private String depositaPreleva = "";

	public DepositaPreleva(String filePath, String depositaPreleva) {
		this.depositaPreleva = depositaPreleva;

		setTitle("DEUTCH BANK");
		setSize(400, 300);
		addWindowListener(new WindowAdapter() {
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

		JLabel portafoglioLabel = new JLabel("Nel portafoglio hai " + portafoglio);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int labelWidth = portafoglioLabel.getPreferredSize().width;

				int x = (400 - labelWidth) / 2;

				int y = 30;

				portafoglioLabel.setBounds(x, y, labelWidth, 25);
			}
		});
		panel.add(portafoglioLabel);

		JLabel contoCorrenteLabel = new JLabel("Nel conto hai " + contoCorrente);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int labelWidth = contoCorrenteLabel.getPreferredSize().width;

				int x = (400 - labelWidth) / 2;

				int y = 60;

				contoCorrenteLabel.setBounds(x, y, labelWidth, 25);
			}
		});
		panel.add(contoCorrenteLabel);

		JLabel depositaPrelevaLabel = new JLabel(depositaPreleva + ":");
		depositaPrelevaLabel.setBounds(50, 100, 100, 25);
		panel.add(depositaPrelevaLabel);

		JTextField depositaPrelevaField = new JTextField();
		depositaPrelevaField.setBounds(150, 100, 200, 25);
		panel.add(depositaPrelevaField);

		JButton confirmButton = new JButton("Conferma");
		confirmButton.setBounds(150, 150, 100, 25);
		panel.add(confirmButton);

		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String soldiString = new String(depositaPrelevaField.getText()).trim();
				actions(filePath, panel, soldiString, settimana, portafoglio, contoCorrente);
			}
		});
	}

	/**
	 * Metodo che racchiude tutte le azioni da svolgere dopo che l'utente ha
	 * schiacciato su "Conferma"
	 *
	 * @param filePath      -- Percorso del file interessato
	 * @param panel         -- JPanel al quale si collega la finestra per mandare
	 *                      messaggi
	 * @param soldiString   -- Variabile che contiene la stringa inserita
	 *                      dall'utente nel campo depositaPrelevaField
	 * @param settimana     -- Settimana attuale dell'operazione
	 * @param portafoglio   -- Valore portafoglio attualmente (prima delle
	 *                      operazioni)
	 * @param contoCorrente -- Valore conto corrente attualmente (prima delle
	 *                      operazioni)
	 */
	private void actions(String filePath, JPanel panel, String soldiString, int settimana, double portafoglio,
			double contoCorrente) {
		soldi = 0;
		boolean giusto = true;

		try {
			soldi = Double.parseDouble(soldiString);
		} catch (NumberFormatException e1) {
			giusto = false;
		}

		double dati[];
		if (depositaPreleva.equals("Deposita")) {
			dati = depositoPrelievo(panel, giusto, portafoglio, contoCorrente, soldi);
		} else {
			dati = depositoPrelievo(panel, giusto, contoCorrente, portafoglio, soldi);
		}

		if (dati == null) {
			JOptionPane.showMessageDialog(panel, "Importo errato", "Errore", JOptionPane.ERROR_MESSAGE);
		} else {
			double nuovoPortafoglio = dati[0], nuovoConto = dati[1];

			try {
				FileTools.aggiungiNuovaRiga(filePath, nuovoPortafoglio, nuovoConto, settimana, "Deposito/Prelievo");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			dispose();
		}

	}

	/**
	 * Metodo che fa depositare o prelevare dei soldi all'utente della banca
	 *
	 * @param panel    -- JPanel al quale si collega la finestra per mandare
	 *                 messaggi
	 * @param giusto   -- Booleana che controlla se il valore inserito dall'utente
	 *                 e' veramente double
	 * @param togli    -- Variabile che contiene il dato da cui si prendono i soldi
	 *                 per spostarli (per deposito = portafoglio, per prelievo =
	 *                 conto corrente)
	 * @param aggiungi -- Variabile che contiene il dato in cui si aggiungono i
	 *                 soldi spostati (per deposito = conto corrente, per prelievo =
	 *                 portafoglio)
	 * @param soldi    -- Variabile che contiene quanti soldi l'utente vuole
	 *                 depositare o prelevare
	 */
	public double[] depositoPrelievo(JPanel panel, boolean giusto, double togli, double aggiungi, double soldi) {
		if (!giusto || (soldi <= 0 || soldi > togli)) {
			JOptionPane.showMessageDialog(panel, "Importo errato", "Errore", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(panel, "Azione svolta");

			double[] dati = sommaSottrazione(aggiungi, togli, soldi); // pos 0 = portafoglio, pos 1 = conto
			if (!depositaPreleva.equals("Deposita")) {
				double nuovoPortafoglio = dati[1];
				dati[1] = dati[0];
				dati[0] = nuovoPortafoglio;
			}
			return dati;
		}
		return null;
	}

	/**
	 * Metodo che permette di sommare e sottrarre un certo valore da due numeri
	 *
	 * @param somma   -- Dato al quale si deve aggiungere un determinato valore
	 * @param sottrai -- Dato al quale si deve sottrarre un determinato valore
	 * @param valore  -- Valore da sommare e sottrarre agli altri dati
	 * @return restituisce un vettore di double nel quale la posizione 0 contiene il
	 *         risultato della sottrazione e la posizione 1 contiene il risultato
	 *         della somma
	 */
	private double[] sommaSottrazione(double somma, double sottrai, double valore) {
		double[] dati = { 0, 0 };
		dati[0] = sottrai - valore;
		dati[1] = somma + valore;
		return dati;
	}

	/**
	 * Metodo che restituisce i soldi che l'utente ha voluto depositare o prelevare
	 *
	 * @return soldi che l'utente ha voluto depositare o prelevare
	 */
	public double getSoldi() {
		return soldi;
	}

}
