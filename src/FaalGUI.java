import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class FaalGUI extends JFrame implements ActionListener {

    private static final String DESTINATION_URL = "https://faal.spclashers.workers.dev/api";
    private final JPanel homePanel = new JPanel();
    private final JPanel faalPanel = new JPanel();
    private final JButton getFaalButton = new JButton("دریافت فال");
    private final JButton backButton = new JButton("بازگشت");

    public FaalGUI() {
        this.setLayout(new GridBagLayout());

        initializeHomePanel();
        this.add(homePanel, new GridBagConstraints());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("فال حافظ");

        this.pack();
        Dimension frameDimension = new Dimension();
        frameDimension.width = frameDimension.height = (int) (this.getSize().getWidth() * 1.5);
        this.setMinimumSize(frameDimension);
        this.revalidate();
        this.repaint();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    public void initializeHomePanel() {
        homePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 5, 20, 5);

        JLabel label1 = new JLabel("نیت کنید ...");
        label1.setFont(new Font("Calibri", Font.BOLD, 25));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label1.setOpaque(true);
        label1.setBackground(Color.green);

        gbc.gridx++;
        JLabel label2 = new JLabel("در صورت اتمام نیت خود، روی دکمه‌ی زیر کلیک کنید.");
        label2.setFont(new Font("Calibri", Font.PLAIN, 15));

        gbc.gridx++;
        getFaalButton.setFont(new Font("Calibri", Font.BOLD, 15));
        getFaalButton.addActionListener(this);
        getFaalButton.setFocusable(false);

        homePanel.add(label1, gbc);
        homePanel.add(label2, gbc);
        homePanel.add(getFaalButton, gbc);
    }


    public void initializeFaalPanel(String poem, String interpretation) {
        faalPanel.removeAll();

        poem = poem.replace("\\r\\n", "\r\n");
        interpretation = interpretation.replace("\\r\\n", "\r\n");

        faalPanel.setLayout(new GridBagLayout());
        Panel poemAndInterpretationPanel = new Panel(new GridLayout(1, 2));

        JLabel label1 = new JLabel("شعر");
        label1.setFont(new Font("Calibri", Font.BOLD, 20));
        label1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label1.setOpaque(true);
        label1.setBackground(Color.YELLOW);


        JTextArea label2 = new JTextArea(poem);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label2.setFont(new Font("Calibri", Font.PLAIN, 17));
        label2.setEditable(false);
        label2.setBackground(null);
        label2.setAlignmentY(Component.CENTER_ALIGNMENT);


        JLabel label3 = new JLabel("تفسیر");
        label3.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        label3.setFont(new Font("Calibri", Font.BOLD, 20));
        label3.setOpaque(true);
        label3.setBackground(Color.YELLOW);


        JTextArea label4 = new JTextArea(interpretation);
        label4.setAlignmentX(Component.CENTER_ALIGNMENT);
        label4.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label4.setEditable(false);
        label4.setLineWrap(true);
        label4.setBackground(null);
        label4.setFont(new Font("Calibri", Font.PLAIN, 17));


        backButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setFont(new Font("Calibri", Font.BOLD, 20));
        backButton.addActionListener(this);

        JPanel poemPanel = new JPanel(new GridBagLayout());
        JPanel interpretationPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        poemPanel.add(label1, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.SOUTH;
        poemPanel.add(label2, gbc);

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.NONE;
        interpretationPanel.add(label3, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.SOUTH;
        interpretationPanel.add(label4, gbc);

        poemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
        interpretationPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));

        poemAndInterpretationPanel.add(interpretationPanel);
        poemAndInterpretationPanel.add(poemPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        faalPanel.add(poemAndInterpretationPanel, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        faalPanel.add(backButton, gbc);
    }

    public Faal fetchFaal() throws IOException, URISyntaxException {
        URI uri = new URI(DESTINATION_URL);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder jsonString = new StringBuilder();
        while ((inputLine = bufferReader.readLine()) != null) {
            jsonString.append(inputLine);
        }
        bufferReader.close();
        connection.disconnect();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString.toString(), Faal.class);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(getFaalButton)) {
            try {
                Faal faal = fetchFaal();
                initializeFaalPanel(faal.getPoem(), faal.getInterpretation());
                this.getContentPane().removeAll();
                this.add(faalPanel, new GridBagConstraints());
                this.pack();
                this.revalidate();
                this.repaint();
            } catch (IOException | URISyntaxException e1) {
                System.out.println("Some error occurred: " + e1.getMessage());
            }
        } else if (e.getSource().equals(backButton)) {
            this.getContentPane().removeAll();
            this.add(homePanel, new GridBagConstraints());
            this.pack();
            this.revalidate();
            this.repaint();
        }
    }
}