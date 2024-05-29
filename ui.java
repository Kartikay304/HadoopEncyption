import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.FlowLayout;

public class ui {

    public static void main(String[] args) {
        // Create a new JFrame
        JFrame frame = new JFrame("Encryption In Hadoop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);

        // Set layout manager for the main frame
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));


        // Create buttons panel with FlowLayout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center-align buttons horizontally
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center-align buttons horizontally
        JPanel centerPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));


        // Create buttons
        JButton button = new JButton("Generate RSA Keys");
        JButton button1 = new JButton("Generate AES Keys");
        JButton button2 = new JButton("Encrypt File");
        JButton button3 = new JButton("Decrypt File");

        JTextField input_plainText = new JTextField(20);
        JTextField input_hadoopOutputFile = new JTextField(20);
        JTextField input_cipherText = new JTextField(20);
        JTextField input_HadoopOutputFile = new JTextField(20);

        // Add ActionListener to the RSA button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand("cd ~/hadoopEncryption && hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.RSA");
            }
        });

        // Add ActionListener to the AES button
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand("cd ~/hadoopEncryption && hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.AES");
            }
        });

        // Add ActionListener to the Encrypt button
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = input_plainText.getText();
                String output = input_hadoopOutputFile.getText();
                String command = "cd ~/hadoopEncryption && hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.aesEncryptJob file:///home/ubuntu/"  + input +" "+ output ;
                executeCommand(command);
            }
        });

        // Add ActionListener to the Decrypt button
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = input_cipherText.getText();
                String output = input_HadoopOutputFile.getText();
                String command = "cd ~/hadoopEncryption && hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.aesDecryptJob file:///home/ubuntu/"  + input +" "+ output ;
                executeCommand(command);
            }
        });

        // Add buttons to the button panel
        topPanel.add(button);
        topPanel.add(button1);
        bottomPanel.add(button2);
        bottomPanel.add(button3);

        centerPanel1.add(new JLabel("Enter the Plain Text file name::"));
        centerPanel1.add(input_plainText);
        centerPanel2.add(new JLabel("Encrypt file will store by name::"));
        centerPanel2.add(input_hadoopOutputFile);
        centerPanel3.add(new JLabel("Enter the Cipher Text file name::"));
        centerPanel3.add(input_cipherText);
        centerPanel4.add(new JLabel("Decrypt file will store by name::"));
        centerPanel4.add(input_HadoopOutputFile);


        // Add components to the main frame
        frame.add(topPanel);
        frame.add(centerPanel1);
        frame.add(centerPanel2);
        frame.add(centerPanel3);
        frame.add(centerPanel4);
        frame.add(bottomPanel);

        // Make the frame visible
        frame.setVisible(true);
    }

    private static void executeCommand(String command) {
        try {
            // Specify the shell command to execute
            String[] cmd = { "/bin/bash", "-c", command };

            // Log the command being executed
            System.out.println("Executing command: " + String.join(" ", cmd));

            // Execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            // Check for errors
            if (exitCode != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
