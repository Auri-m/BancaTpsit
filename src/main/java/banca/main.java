/*
 *@author  alvise sacconato
 *@author2 alessandro zago
 *@version 1.2
 *@description tramite questo programma si puo' gestire una banca monoutente con opzioni di prelievo, deposito,
 *			   investimento con diversi tipi di durata ovvero: breve media e lunga e con diversi tipi di rischio,
 *			   ovvero: basso rischio/guadagno, medio rischio/guadagno e alto rischio/guadagno, permette di avanzare di tempo (mesi),
 *			   e di visualizzare le info del conto corrente.
 */

package banca;
import java.util.Scanner;

public class main {

	// Racchiude il try del MenuIniziale, del MenuBanca, del menu Investimento durata e rischio
	public static short TryMenu(int min, int max) {
		Scanner tastiera = new Scanner(System.in);

		short selezione = -1;

		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.print("Selezione: \n");
				selezione = tastiera.nextShort();

				if (selezione >= min && selezione <= max) {
					inputValido = true;
				} else {
					System.out.println("Selezionare un'opzione valida!");
				}
			} catch (Exception e) {
				System.out.println("Errore: Inserire un numero valido!");
				tastiera.next(); // Salta tasto "invio" nel buffer
			}
		}
		return selezione;
	}

	/**
	 * Menu iniziale di avvio del programma
	 * 
	 * @return -- Ritorna la decisione prese dall'utente
	 */
	public static short MenuIniziale() {
		System.out.println("BENVENUTO NELLA BANCA!");
		System.out.println("Cosa desideri fare?\n");

		System.out.println("1)Accedi al conto corrente");
		System.out.println("0)Esci");

		short selezione = -1;
		selezione = TryMenu(0, 1); // 0 e 1 corrispettivi valori min e max del menu

		return selezione;
	}

	/**
	 * Menu per scegliere l'operazione da fare con la banca
	 * 
	 * @param Utente -- Vettore utente
	 * @return -- Ritorna l'operazione della banca che vuole fare l'utente
	 */
	public static short MenuBanca(String Utente[]) {
		Scanner tastiera = new Scanner(System.in);

		System.out.println("Ciao " + Utente[0] + " Cosa desideri fare?");
		System.out.println("1)Visualizza stato del conto e del portafoglio");
		System.out.println("2)Deposita Soldi");
		System.out.println("3)Preleva Soldi");
		System.out.println("4)Fai un investimento");
		System.out.println("5)Avanza di X Mesi \n");

		System.out.println("0)Esci dalla banca");

		short selezione = -1;
		selezione = TryMenu(0, 5); // 0 e 1 corrispettivi valori min e max del menu

		return selezione;
	}

	/**
	 * Metodo per inserire le credenziali di accesso (nome utente e password)
	 * 
	 * @param Utente         -- Vettore utente
	 * @param NameOrPassword -- Serve per indicare se si vuole inserire il nome[0] o
	 *                       la password[1]
	 */
	public static void Utente(String Utente[], int NameOrPassword) {
		Scanner tastiera = new Scanner(System.in);

		boolean isPassword = false;

		boolean inputValido = false;
		while (!inputValido) {
			try {
				if (NameOrPassword == 0) {
					System.out.println("Inserisci nome utente: ");
				} else {
					System.out.println("Inserisci la password: ");
					isPassword = true;
				}

				Utente[NameOrPassword] = tastiera.nextLine();

				if (!Utente[NameOrPassword].trim().isEmpty()) { // Se la stringa ha un valore assegnato vai avanti, altrimenti no
					inputValido = true;
				} else {
					System.out.println("Selezionare un'opzione valida!");
				}
			} catch (Exception e) {
				System.out.println("Errore: Inserire un numero valido!");
				tastiera.next(); // Salta tasto "invio" nel buffer
			}
		}
		if (isPassword = true) {
			System.out.println("ATTENZIONE!! ricordati la password! \nPremi un tasto per continuare... \n");
		}
	}

	/**
	 * Metodo per visualizzare le informazione dell'utente in banca (Nome, Soldi nel
	 * conto e portafoglio, mesi trascorsi)
	 * 
	 * @param Utente        -- Vettore utente
	 * @param Soldi         -- Serve per far vedere soldi in banca[0] o nel
	 *                      portafoglio[1]
	 * @param ContatoreMesi -- Tiene conto dei mesi trascorsi da quando è entrato
	 *                      nella banca
	 */
	public static void InfoConto(String Utente[], double Soldi[], int ContatoreMesi) {
		System.out.println("Nome: " + Utente[0]);
		System.out.println("Saldo in banca: " + Soldi[0]);
		System.out.println("Saldo nel portafoglio: " + Soldi[1]);
		System.out.println("Mesi trascorsi dall'appertura del conto: " + ContatoreMesi + "\n");
	}

	/**
	 * Metodo per generare un numero random, cosi da randomizzare alcuni eventi,
	 * come saldo iniziale e le probabilita
	 * 
	 * @param max -- Valore massimo del random
	 * @param min -- Valore minimo del random
	 * @return -- Ritorna un numero random compreso tra i valori richiesti prima
	 */
	public static double GeneraRandom(double max, double min) {
		// double gen = min + (Math.random() * (max - min)); // Genera il numero random
		// double random = Math.round(gen * 100.0) / 100.0; // Lascia solo 2 cifre
		// decimali

		double random = Math.round((min + (Math.random() * max)) * 100) / 100; // Fa entrambe le cose

		return random;
	}

	// Metodo per verificare la password inserita dall'utente
	public static void ChiediPassword(String Utente[]) {
		Scanner tastiera = new Scanner(System.in);
		String scelta;

		System.out.println("Inserisci la password per continuare -->");
		scelta = tastiera.nextLine();

		while (scelta.equals(Utente[1]) != true) {
			System.out.println("La password e' sbagliata! \nReinserisci -->");
			scelta = tastiera.nextLine();
		}

		System.out.println("Password corretta!\n");
	}

	// Metodo Verfica(try) deposito/ritiro soldi
	public static double DepositoRitiro(double Soldi[], int DepositoOrRitiro) {
		Scanner tastiera = new Scanner(System.in);

		double SoldiInseriti = -1;

		boolean inputValido = false;
		while (!inputValido) {
			try {
				if (DepositoOrRitiro == 0) {
					System.out.println("Quanti soldi vuoi depositare? -->");
				} else {
					System.out.println("Quanti soldi vuoi ritirare? -->");
				}

				SoldiInseriti = tastiera.nextDouble();

				if (SoldiInseriti > 0 && SoldiInseriti <= Soldi[1]) { // Se la stringa ha un valore assegnato vai avanti, altrimenti no
					inputValido = true;
				} else {
					System.out.println(
							"Non puoi inserire un importo negativo oppure hai inserito piu' soldi di quelli che hai!\n");
				}
			} catch (Exception e) {
				System.out.println("Errore: Inserire un numero valido!");
				tastiera.next(); // Salta tasto "invio" nel buffer
			}
		}
		return SoldiInseriti;
	}

	/**
	 * Metodo che permettere di depositare i soldi in banca dal portafoglio
	 * 
	 * @param Soldi -- Vettore soldi
	 */
	public static void DepositoSoldi(double Soldi[]) {
		double SoldiInseriti = DepositoRitiro(Soldi, 0); // 0 (banca) se sono da depositare 1 (portafoglio) se sono da ritirare

		Soldi[0] = Soldi[0] + SoldiInseriti;
		Soldi[1] = Soldi[1] - SoldiInseriti;
		System.out.println("Hai depositato: " + SoldiInseriti + "€");
	}

	// Metodo che permettere di prelevare soldi dalla banca
	public static void PrelievoSoldi(double Soldi[]) {
		double SoldiInseriti = DepositoRitiro(Soldi, 1); // 0 (banca) se sono da depositare 1 (portafoglio) se sono da ritirare

		Soldi[1] = Soldi[1] + SoldiInseriti;
		Soldi[0] = Soldi[0] - SoldiInseriti;
		System.out.println("Hai ritirato: " + SoldiInseriti + "€");
	}

	/**
	 * Metodo che permette di andare avanti di n mesi
	 * 
	 * @param ContatoreMesi    -- Contatore mesi
	 * @param Soldi            -- Vettore soldi
	 * @param MesiDaAggiungere -- Passa il numero di mesi che si vuole andare avanti
	 * @return ContatoreMesi -- Ritorna il contatore dei mesi aggiornato
	 */
	public static int AvanzaMesi(int ContatoreMesi, double Soldi[], int MesiDaAggiungere) {
		Scanner tastiera = new Scanner(System.in);

		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.println("Di quanti mesi vuoi avanzare --> ");
				MesiDaAggiungere = tastiera.nextShort();

				if (MesiDaAggiungere >= 0) {
					inputValido = true;
				} else {
					System.out.println("Devi aggiungere almeno un mese!");
				}
			} catch (Exception e) {
				System.out.println("Errore: Inserire un valore valido!");
				tastiera.next(); // Salta tasto "invio" nel buffer
			}
		}

		ContatoreMesi = ContatoreMesi + MesiDaAggiungere;
		Soldi[1] = Soldi[1] + (100 * MesiDaAggiungere); // serve per aggiungere ogni mese 100 euro.

		System.out.println("Hai avanzato di: " + MesiDaAggiungere + "!");

		return ContatoreMesi;
	}

	// Metodo che gestisce tutta la parte dell'investimento
	public static void Investimento(double Soldi[], int ContatoreMesi) {
		Scanner tastiera = new Scanner(System.in);

		int NGenerato = (int) GeneraRandom(10, 1);

		double SoldiDaInvestire = 0, SoldiVinti = 0;
		short DurataInvestimento, RischioInvestimento;

		boolean inputValido = false;
		while (!inputValido) {
			try {
				System.out.println("Inserisci quanti soldi vuoi investire -->");
				SoldiDaInvestire = tastiera.nextDouble();

				if (SoldiDaInvestire >= 0) {
					inputValido = true;
				} else {
					System.out.println("Non hai abbastanza soldi!");
				}
			} catch (Exception e) {
				System.out.println("Errore: Inserire un valore valido!");
				tastiera.next(); // Salta tasto "invio" nel buffer
			}
		}

		System.out.println("1)Investimento di Breve durata");
		System.out.println("2)Investimento di Media durata");
		System.out.println("3)Investimento di Lunga durata");
		System.out.println("Selezione: ");

		DurataInvestimento = TryMenu(1, 3);

		System.out.println("1)Investimento di Basso Rischio e Basso Guadagno");
		System.out.println("2)Investimento di Medio Rischio e Medio Guadagno");
		System.out.println("3)Investimento di Alto Rischio e Alto Guadagno");
		System.out.println("Selezione: ");

		RischioInvestimento = TryMenu(1, 3);

		switch (RischioInvestimento) {
			case 1: {
				if (NGenerato <= '8') { // tanta probabilita' ha solo il 20% di possibile perdita
					System.out.println("Hai vinto!");
	
					SoldiVinti = (SoldiDaInvestire / 100) * GeneraRandom(25, 1);
					Soldi[0] = Soldi[0] + SoldiVinti;
				} else {
					System.out.print("Hai perso!");
					Soldi[0] = Soldi[0] - SoldiDaInvestire;
				}
				break;
			}
			case 2: {
				if (NGenerato <= '5') { // media probabilita' ha il 50% di possibile perdita
					System.out.println("Hai vinto!");
	
					SoldiVinti = (SoldiDaInvestire / 100) * GeneraRandom(50, 15);
					Soldi[0] = Soldi[0] + SoldiVinti;
				} else {
					System.out.print("Hai perso!");
					Soldi[0] = Soldi[0] - SoldiDaInvestire;
				}
				break;
			}
			case 3: {
				if (NGenerato <= '2') {// poca probabilita' ha l'80% di possibile perdita
					System.out.println("Hai vinto!");
	
					SoldiVinti = (SoldiDaInvestire / 100) * GeneraRandom(100, 40);
					Soldi[0] = Soldi[0] + SoldiVinti;
				} else {
					System.out.print("Hai perso!");
					Soldi[0] = Soldi[0] - SoldiDaInvestire;
				}
				break;
			}
		}

		AvanzaMesi(ContatoreMesi, Soldi, DurataInvestimento);

		System.out.println("Durata investimento: " + DurataInvestimento);
		System.out.println("Hai invesito: " + SoldiDaInvestire);
		System.out.println("Hai Guadagnato: " + SoldiVinti);
		System.out.println("Totale nel conto corrente: " + Soldi[0]);
	}

	// MAIN
	public static void main(String[] args) {
		Scanner tastiera = new Scanner(System.in);

		// Inizializzazione variabili globali

		String Utente[] = new String[2]; // [0] = Nome Utente - [1] = Password

		double Soldi[] = new double[2]; // [0] = SOLDI BANCA - [1] = SOLDI PORTAFOGLIO
		Soldi[1] = 100; // Inizializza soldi nel portafoglio
		Soldi[0] = GeneraRandom(75, 10); // Genera soldi random nel conto corrente

		int ContatoreMesi = 0;

		// Esecuzione di tutto il programma
		while (true) {

			short selezione = MenuIniziale(); // Mostra il Menu iniziale di avvio del programma

			switch (selezione) {
				case 1: {
					boolean esci = false; // Serve per uscire dal while e quindi da questo sotto_case e tornare nel case principale
	
					Utente(Utente, 0); // Login dell'utente (nome)
					Utente(Utente, 1); // Login dell'utente (password)
	
					// Ciclo per le operazioni nel conto corrente
					while (!esci) {
	
						short selezione2 = MenuBanca(Utente); // Assegna ad una variabile l'operazione da fare scelta nel metodo menu + Stampa il menu
	
						if (selezione2 != '0') { // Serve per non far stampare le info nel conto nel case 0 che e' dedicato alla chiusura del sottomenu
							InfoConto(Utente, Soldi, ContatoreMesi);
						}
	
						// Gestione delle opzioni visionate nel menu "operazioni"
						switch (selezione2) {
							case 1: {
								InfoConto(Utente, Soldi, ContatoreMesi);
								break;
							}
							case 2: {
								ChiediPassword(Utente);
								DepositoSoldi(Soldi);
								break;
							}
							case 3: {
								ChiediPassword(Utente);
								PrelievoSoldi(Soldi);
								break;
							}
							case 4: {
								ChiediPassword(Utente);
								Investimento(Soldi, ContatoreMesi);
								break;
							}
							case 5: { // Permette di uscire da questo sotto_menu
								ChiediPassword(Utente);
		
								System.out.println("Sei sicuro di voler continuare? (Y = si, N = no)");
								String scelta = tastiera.next();
		
								if (scelta.equalsIgnoreCase("Y")) {
									ContatoreMesi = AvanzaMesi(ContatoreMesi, Soldi, 0); // 0 sono i mesi da aggiungere
								} else {
									System.out.println("Hai scelto di non continuare!");
								}
								break;
							}
							case 0: { // Fa uscire da questo switch (sotto menu) (gestione conto corrente)
								System.out.println("Sei sicuro di voler uscire? (Y = si, N = no)");
								String scelta = tastiera.next();
		
								if (scelta.equalsIgnoreCase("Y")) {
									System.out.println("Sei uscito!");
									esci = true;
								} else {
									System.out.println("Hai scelto di non uscire!");
								}
								break;
							}
						}
					}
					break;
				}
				case 0: { // Fa chiudere il programma
					System.out.println("Sei sicuro di voler uscire? (Y = si, N = no)");
					String scelta = tastiera.next();
	
					if (scelta.equalsIgnoreCase("Y")) {
						System.out.println("Sei uscito!");
						System.exit(0); // Chiude il programma
					} else {
						System.out.println("Hai scelto di non uscire!");
					}
					break;
				}
			}
		}
	}
}
