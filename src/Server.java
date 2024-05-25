import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server implements ActionListener {
    JTextField text;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static Connection conn;

    Server() {
        f.setLayout(null);
        JPanel P1 = new JPanel();
        P1.setBackground(new Color(7, 94, 84));
        P1.setBounds(0, 0, 450, 70);
        P1.setLayout(null);
        f.add(P1);

        JLabel back = createIconLabel("icons/3.png", 25, 25, 5, 20, 25, 25);
        P1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        JLabel profile = createIconLabel("icons/22.png", 50, 50, 40, 10, 50, 50);
        P1.add(profile);

        JLabel video = createIconLabel("icons/video.png", 30, 30, 300, 20, 30, 30);
        P1.add(video);

        JLabel phone = createIconLabel("icons/phone.png", 35, 30, 360, 20, 35, 30);
        P1.add(phone);

        JLabel morevert = createIconLabel("icons/3icon.png", 10, 25, 420, 20, 10, 25);
        P1.add(morevert);

        JLabel name = new JLabel("Akash Sir");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        P1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 13));
        P1.add(status);

        a1 = new JPanel();
        a1.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(a1);
        scrollPane.setBounds(5, 75, 440, 570);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        f.add(scrollPane);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);
        f.setVisible(true);

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_application", "root", "Arupda$1023");
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadMessages1();
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText();
            if (!out.equals("")) {
                storeMessage("Akash Sir", "", out);

                JPanel P2 = formatLabel(out);

                JPanel right = new JPanel(new BorderLayout());
                right.add(P2, BorderLayout.LINE_END);
                vertical.add(right);
                vertical.add(Box.createVerticalStrut(15));

                a1.add(vertical, BorderLayout.PAGE_START);

                text.setText("");
                f.repaint();
                f.invalidate();
                f.validate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);
        return panel;
    }

    public void storeMessage(String sender, String receiver, String message) {
        try {
            String query = "INSERT INTO messages (sender, receiver, message) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, sender);
            pstmt.setString(2, receiver);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadMessages1() {
        try {
            while (true){
                Thread.sleep(1000);
                vertical.removeAll();
                a1.removeAll();
                String query = "SELECT * FROM messages";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String sender = rs.getString("sender");
                    String receiver = rs.getString("receiver");
                    String message = rs.getString("message");
                    String formattedMessage = String.format("%s: %s", sender, message);

                    JPanel panel = formatLabel(formattedMessage);

                    JPanel align = new JPanel(new BorderLayout());
                    if (sender.equals("Akash Sir")) {
                        align.add(panel, BorderLayout.LINE_END);
                    } else {
                        align.add(panel, BorderLayout.LINE_START);
                    }
                    vertical.add(align);
                    vertical.add(Box.createVerticalStrut(15));
                }
                a1.add(vertical, BorderLayout.PAGE_START);
                f.validate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public JLabel createIconLabel(String path, int width, int height, int x, int y, int labelWidth, int labelHeight) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(path));
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        ImageIcon newIcon = new ImageIcon(img);
        JLabel label = new JLabel(newIcon);
        label.setBounds(x, y, labelWidth, labelHeight);
        return label;
    }

    public static void main(String[] args) {
        new Server();
    }
}
