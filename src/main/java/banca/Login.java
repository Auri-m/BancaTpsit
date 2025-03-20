package banca;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Login extends JFrame {

    private String nome;

    public Login(String filePath) {
        setTitle("DEUTCH BANK");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(150, 50, 200, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nome = userText.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String filePathDefinitivo = filePath + nome + ".csv";
                String pathGrafico = filePath + nome + "_grafico.csv";
                File utente = new File(filePathDefinitivo);
                File grafico = new File(pathGrafico);

                if (utente.exists()) {
                    login(panel, utente, filePathDefinitivo, password);
                } else {
                    double conto = creazione(utente, password);
                    fileGrafico(panel, grafico, conto);
                }

            }
        });
    }

    /**
     * Metodo per accedere a un file che ha come nome utente la stringa indicata nel
     * campo userText. Qui viene controllato se la password coincide e se l'account
     * non e' in esecuzione. Se si' viene modificata la prima riga del file,
     * impostando il flag a 1 per indicare che l'account e' in esecuzione. Se no
     * mostra un messaggio di errore
     *
     * @param panel    -- JPanel al quale si collega la finestra per mandare
     *                 messaggi
     * @param filePath -- Percorso del file interessato
     * @param utente   -- variabile File per controllare la password presente al suo
     *                 interno
     * @param password -- variabile contenete la password dell'account
     */
    private void login(JPanel panel, File utente, String filePath, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(utente))) {

            String[] dati = br.readLine().split(";");
            String passwordUtente = dati[0];
            String open = dati[1];

            if (open.equals("0") && passwordUtente.equals(password)) {
                JOptionPane.showMessageDialog(panel, "Login riuscito!");
                FileTools.esciEntra(filePath, 1);
                dispose();
            } else {
                JOptionPane.showMessageDialog(panel, "Password errata o account in esecuzione.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo che crea un file per un nuovo utente contenente: una prima riga con
     * password e flag a 1 che indica che l'account e' in esecuzione e una seconda
     * riga con saldo portafoglio iniziale (100.0), saldo conto corrente iniziale
     * (tra 10 e 75), 1 per dire che e' la prima settimana e la parola "Creazione"
     * per indicare che è il momento in cui e' stato creato l'account
     *
     * @param panel    -- JPanel al quale si collega la finestra per mandare
     *                 messaggi
     * @param utente   -- variabile File per la creazione di un nuovo file
     * @param password -- variabile contenete la password del nuovo account
     */
    private double creazione(File utente, String password) {
        try {
            utente.createNewFile();
            double conto = FileTools.GeneraRandom(75, 10);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(utente))) {
                writer.write(password + ";" + 1);
                writer.newLine();

                writer.write(1 + ";" + conto + ";" + 100.0 + ";" + "Creazione");
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            return conto;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return 0;
    }

    private void fileGrafico(JPanel panel, File grafico, double conto) {
        try {
            grafico.createNewFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(grafico))) {
                writer.write(0 + ";" + conto + ";" + 100.0 + ";" + "Creazione");
            } catch (IOException e2) {
                e2.printStackTrace();
            }

            JOptionPane.showMessageDialog(panel, "Registrazione avvenuta!");
            dispose();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Metodo che restituisce il nome inserito nel campo userText
     *
     * @return nome utente
     */
    public String getNome() {
        return nome;
    }

}
