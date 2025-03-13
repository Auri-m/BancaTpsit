/*
 *@author aurora masenello
 *@version 1.2
 *@description tramite questo programma si puo' gestire una banca monoutente con opzioni di prelievo, deposito,
 *			   investimento con diversi tipi di durata ovvero: breve media e lunga e con diversi tipi di rischio,
 *			   ovvero: basso rischio/guadagno, medio rischio/guadagno e alto rischio/guadagno, permette di avanzare di tempo (mesi),
 *			   e di visualizzare le info del conto corrente.
 */

package banca;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner tastiera = new Scanner(System.in);

		String filePath = "C:\\Users\\Utente\\Documents\\AURORA\\BancaTpsit\\";
		short selezione;

		do {
			boolean ok;
			do {
				ok = true;
				selezione = Menu.MenuIniziale();

			} while (!ok);

			switch (selezione) {

			case 1: {

				Login login = new Login(filePath);
				login.setVisible(true);

				login.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						String file = filePath + login.getNome() + ".csv";
						HomePage homepage = new HomePage(file, login.getNome());
						homepage.setVisible(true);
					}
				});

				break;
			}

			case 0: {
				System.out.print("Sei sicuro di voler uscire? (Y = si, Others = no)  ");
				String scelta = tastiera.next();

				if (scelta.equalsIgnoreCase("Y")) {
					System.out.println("Sei uscito!");
				} else {
					selezione = 1;
					System.out.println("Hai scelto di non uscire!");
				}
				break;
			}
			}
		} while (selezione != 0);

		tastiera.close();
	}
}