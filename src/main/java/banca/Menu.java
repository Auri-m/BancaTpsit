package banca;

import java.util.Scanner;

public class Menu {

    // Racchiude il try del MenuIniziale, del MenuBanca, del menu Investimento
    // durata e rischio
    public static short TryMenu(int min, int max) {
        Scanner tastiera = new Scanner(System.in);

        short selezione = -1;
        boolean inputValido = true;

        do {
            System.out.print("Selezione: ");

            try {
                selezione = tastiera.nextShort();
            } catch (Exception e) {
                System.out.println("Errore: Inserire un numero numerico!");
                inputValido = false;
                tastiera.next();
            }

            if (inputValido && (selezione < min || selezione > max)) {
                inputValido = false;
                System.out.println("Selezionare un'opzione valida!");
            }

        } while (!inputValido);

        return selezione;
    }

    /**
     * Menu iniziale di avvio del programma
     *
     * @return -- Ritorna la decisione prese dall'utente
     */
    public static short MenuIniziale() {
        System.out.println("\nBENVENUTO IN DEUTSCHE BANK!");
        System.out.println("Cosa desideri fare?\n");

        System.out.println("1)Accedi/Registrati");
        System.out.println("0)Esci");

        return Menu.TryMenu(0, 1);
    }

}
