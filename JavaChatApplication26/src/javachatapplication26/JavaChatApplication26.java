package javachatapplication26;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JavaChatApplication26 {

    private JPanel chatDisplayPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane scrollPane;

    public JavaChatApplication26() {
        JFrame frame = new JFrame("WhatsApp");
        frame.setSize(450, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Chat display panel
        chatDisplayPanel = new JPanel();
        chatDisplayPanel.setLayout(new BoxLayout(chatDisplayPanel, BoxLayout.Y_AXIS));
        chatDisplayPanel.setBackground(Color.black);

        scrollPane = new JScrollPane(chatDisplayPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Message input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        messageField = new JTextField();
        messageField.setBackground(Color.LIGHT_GRAY);
        messageField.setForeground(Color.BLACK);
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Send");
        sendButton.setBackground(Color.GREEN);
        sendButton.setForeground(Color.WHITE);

        ActionListener sendActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    appendMessage("You", message);
                    messageField.setText("");
                }
            }
        };

        sendButton.addActionListener(sendActionListener);
        messageField.addActionListener(sendActionListener);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add components to frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void appendMessage(String sender, String message) {
        JPanel messageBubble = new JPanel();
        messageBubble.setLayout(new BorderLayout());
        messageBubble.setBackground(new Color(0, 128, 0)); // Green background
        messageBubble.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Get current time for timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String timestamp = now.format(formatter);

        // Message label
        JLabel messageLabel = new JLabel("<html><p style=\"width: 200px\">" + sender + ": " + message + "</p></html>");
        messageLabel.setForeground(Color.white);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Timestamp label
        JLabel timestampLabel = new JLabel(timestamp);
        timestampLabel.setForeground(Color.lightGray);
        timestampLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(timestampLabel, BorderLayout.EAST);  // Align timestamp to the bottom right

        messageBubble.add(messageLabel, BorderLayout.CENTER);
        messageBubble.add(bottomPanel, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout(sender.equals("You") ? FlowLayout.RIGHT : FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.add(messageBubble);

        chatDisplayPanel.add(wrapper);
        chatDisplayPanel.revalidate();
        chatDisplayPanel.repaint();
    }

    public static void main(String[] args) {
        new JavaChatApplication26();
    }
}
