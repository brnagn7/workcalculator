package net.edmooney;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class WorkCalculatorGUI extends JFrame {
    private final double normalRate = 26.25;
    private final double overtimeRate = 37.50;
    private final int normalHoursLimit = 40;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Light Blue
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private final Color TEXT_COLOR = new Color(44, 62, 80);         // Dark Blue
    private final Color ACCENT_COLOR = new Color(46, 204, 113);     // Green

    private JTextField[][] dayInputs;
    private JLabel[][] weekLabels;
    private JLabel totalPayLabel;
    private JLabel totalHoursLabel;
    private JLabel overtimeHoursLabel;
    private JLabel normalPayLabel;
    private JLabel overtimePayLabel;

    public WorkCalculatorGUI() {
        setTitle("Work Hours Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Initialize arrays
        dayInputs = new JTextField[2][5];
        weekLabels = new JLabel[2][2];

        // Create main panel with custom background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create content panel for weeks
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Add title with custom styling
        JLabel titleLabel = new JLabel("Work Hours Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        contentPanel.add(titleLabel, gbc);

        // Create input fields for each week
        for (int week = 0; week < 2; week++) {
            // Week panel with border
            JPanel weekPanel = new JPanel(new GridBagLayout());
            weekPanel.setBackground(Color.WHITE);
            weekPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            GridBagConstraints weekGbc = new GridBagConstraints();
            weekGbc.insets = new Insets(5, 5, 5, 5);
            
            // Week label
            JLabel weekLabel = new JLabel("Week " + (week + 1));
            weekLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            weekLabel.setForeground(PRIMARY_COLOR);
            weekGbc.gridx = 0;
            weekGbc.gridy = 0;
            weekGbc.gridwidth = 5;
            weekPanel.add(weekLabel, weekGbc);

            // Day labels and inputs
            String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
            for (int day = 0; day < 5; day++) {
                weekGbc.gridy = 1;
                weekGbc.gridx = day;
                weekGbc.gridwidth = 1;
                
                JLabel dayLabel = new JLabel(days[day]);
                dayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                dayLabel.setForeground(TEXT_COLOR);
                weekPanel.add(dayLabel, weekGbc);

                weekGbc.gridy = 2;
                dayInputs[week][day] = new JTextField(5);
                styleTextField(dayInputs[week][day]);
                dayInputs[week][day].setColumns(5);
                weekPanel.add(dayInputs[week][day], weekGbc);
            }

            // Week summary labels
            weekGbc.gridy = 3;
            weekGbc.gridx = 0;
            weekGbc.gridwidth = 5;
            JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            summaryPanel.setBackground(Color.WHITE);
            
            weekLabels[week][0] = new JLabel("Total Hours: 0.0");
            weekLabels[week][1] = new JLabel("Overtime Hours: 0.0");
            styleLabel(weekLabels[week][0]);
            styleLabel(weekLabels[week][1]);
            
            summaryPanel.add(weekLabels[week][0]);
            summaryPanel.add(Box.createHorizontalStrut(20));
            summaryPanel.add(weekLabels[week][1]);
            weekPanel.add(summaryPanel, weekGbc);

            // Add week panel to content panel
            gbc.gridy = week + 1;
            gbc.gridx = 0;
            gbc.gridwidth = 6;
            contentPanel.add(weekPanel, gbc);
        }

        // Add content panel to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Create button panel with a border to make it more visible
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Calculate button with custom styling
        JButton calculateButton = new JButton("Calculate");
        styleButton(calculateButton);
        calculateButton.setPreferredSize(new Dimension(200, 40)); // Make button larger
        buttonPanel.add(calculateButton);

        // Create a wrapper panel for the button to ensure proper spacing
        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setBackground(BACKGROUND_COLOR);
        buttonWrapper.add(buttonPanel, BorderLayout.CENTER);

        // Add button wrapper to main panel
        mainPanel.add(buttonWrapper, BorderLayout.SOUTH);

        // Results panel with custom styling
        JPanel resultsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                "Summary for 2-Week Period",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        totalHoursLabel = new JLabel("Total Normal Hours: 0.0");
        overtimeHoursLabel = new JLabel("Total Overtime Hours: 0.0");
        normalPayLabel = new JLabel("Total Normal Pay: $0.00");
        overtimePayLabel = new JLabel("Total Overtime Pay: $0.00");
        totalPayLabel = new JLabel("Total Pay: $0.00");

        // Style all result labels
        styleResultLabel(totalHoursLabel);
        styleResultLabel(overtimeHoursLabel);
        styleResultLabel(normalPayLabel);
        styleResultLabel(overtimePayLabel);
        styleResultLabel(totalPayLabel);

        resultsPanel.add(totalHoursLabel);
        resultsPanel.add(overtimeHoursLabel);
        resultsPanel.add(normalPayLabel);
        resultsPanel.add(overtimePayLabel);
        resultsPanel.add(totalPayLabel);

        // Add panels to frame
        add(mainPanel, BorderLayout.CENTER);
        add(resultsPanel, BorderLayout.SOUTH);

        // Add calculate button action
        calculateButton.addActionListener(e -> calculateResults());

        // Set frame properties
        setSize(700, 700); // Increased height to accommodate all components
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setPreferredSize(new Dimension(80, 35));
        textField.setMinimumSize(new Dimension(80, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Add focus listener for better visual feedback
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_COLOR);
    }

    private void styleResultLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(ACCENT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

    private void calculateResults() {
        double[] weeklyHours = new double[2];
        double[] weeklyOvertimeHours = new double[2];
        DecimalFormat df = new DecimalFormat("0.00");

        // Calculate for each week
        for (int week = 0; week < 2; week++) {
            double totalWeeklyHours = 0;

            // Sum up daily hours
            for (int day = 0; day < 5; day++) {
                try {
                    double hours = Double.parseDouble(dayInputs[week][day].getText());
                    if (hours < 0) {
                        showError("Hours cannot be negative!");
                        return;
                    }
                    totalWeeklyHours += hours;
                } catch (NumberFormatException ex) {
                    showError("Please enter valid numbers for all hours!");
                    return;
                }
            }

            // Calculate normal and overtime hours
            if (totalWeeklyHours > normalHoursLimit) {
                weeklyHours[week] = normalHoursLimit;
                weeklyOvertimeHours[week] = totalWeeklyHours - normalHoursLimit;
            } else {
                weeklyHours[week] = totalWeeklyHours;
                weeklyOvertimeHours[week] = 0;
            }

            // Update week labels
            weekLabels[week][0].setText("Total Hours: " + df.format(totalWeeklyHours));
            weekLabels[week][1].setText("Overtime Hours: " + df.format(weeklyOvertimeHours[week]));
        }

        // Calculate totals
        double totalNormalHours = weeklyHours[0] + weeklyHours[1];
        double totalOvertimeHours = weeklyOvertimeHours[0] + weeklyOvertimeHours[1];
        double totalNormalPay = totalNormalHours * normalRate;
        double totalOvertimePay = totalOvertimeHours * overtimeRate;
        double totalPay = totalNormalPay + totalOvertimePay;

        // Update result labels
        totalHoursLabel.setText("Total Normal Hours: " + df.format(totalNormalHours));
        overtimeHoursLabel.setText("Total Overtime Hours: " + df.format(totalOvertimeHours));
        normalPayLabel.setText("Total Normal Pay: $" + df.format(totalNormalPay));
        overtimePayLabel.setText("Total Overtime Pay: $" + df.format(totalOvertimePay));
        totalPayLabel.setText("Total Pay: $" + df.format(totalPay));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new WorkCalculatorGUI().setVisible(true);
        });
    }
} 