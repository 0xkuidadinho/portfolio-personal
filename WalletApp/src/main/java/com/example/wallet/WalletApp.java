package com.example.wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WalletApp {

    private JFrame frame;
    private JTextField addressField, tokenNameField, mintTimeField, deployTimeField, supplyField,
            holdPercentageField, athField, durationField;

    private Workbook workbook;
    private Sheet sheet;
    private int rowNum = 0;

    private static final String FILE_NAME = "wallets.xlsx";

    public WalletApp() {
        initializeUI();
        initializeExcel();
        readExistingWallets(); // Read existing wallets if file exists
    }

    private void initializeUI() {
        frame = new JFrame("Wallet App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 2, 10, 10)); // GridLayout with 2 columns

        // Labels and TextFields for wallet attributes
        frame.add(new JLabel("Address:"));
        addressField = new JTextField();
        frame.add(addressField);

        frame.add(new JLabel("Token Name:"));
        tokenNameField = new JTextField();
        frame.add(tokenNameField);

        frame.add(new JLabel("Mint Time:"));
        mintTimeField = new JTextField();
        frame.add(mintTimeField);

        frame.add(new JLabel("Deploy Time:"));
        deployTimeField = new JTextField();
        frame.add(deployTimeField);

        frame.add(new JLabel("Supply:"));
        supplyField = new JTextField();
        frame.add(supplyField);

        frame.add(new JLabel("Hold Percentage:"));
        holdPercentageField = new JTextField();
        frame.add(holdPercentageField);

        frame.add(new JLabel("ATH:"));
        athField = new JTextField();
        frame.add(athField);

        frame.add(new JLabel("Duration:"));
        durationField = new JTextField();
        frame.add(durationField);

        // Button to add wallet
        JButton addButton = new JButton("Add Wallet");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWallet();
            }
        });
        frame.add(addButton);

        // Show the frame
        frame.setVisible(true);
    }

    private void initializeExcel() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                workbook = WorkbookFactory.create(inputStream);
                sheet = workbook.getSheetAt(0); // Assuming first sheet is "Wallets"
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Wallets");

            // Create header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"Address", "Token Name", "Mint Time", "Deploy Time", "Supply", 
                                "Hold Percentage", "ATH", "Duration"};
            int colNum = 0;
            for (String header : headers) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(header);
            }
        }

        // If sheet is null (file was empty or corrupted), create a new sheet
        if (sheet == null) {
            sheet = workbook.createSheet("Wallets");
            
            // Create header row if sheet was created newly
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"Address", "Token Name", "Mint Time", "Deploy Time", "Supply", 
                                "Hold Percentage", "ATH", "Duration"};
            int colNum = 0;
            for (String header : headers) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(header);
            }
        }

        // Determine the next available row number
        rowNum = sheet.getLastRowNum() + 1;
    }

    private void readExistingWallets() {
        try {
            FileInputStream inputStream = new FileInputStream(FILE_NAME);
            workbook = WorkbookFactory.create(inputStream);
            sheet = workbook.getSheetAt(0); // Assuming first sheet is "Wallets"

            // Read existing wallets if any
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip header
                Row row = sheet.getRow(i);
                if (row != null) {
                    String address = row.getCell(0).getStringCellValue();
                    String tokenName = row.getCell(1).getStringCellValue();
                    String mintTime = row.getCell(2).getStringCellValue();
                    String deployTime = row.getCell(3).getStringCellValue();
                    String supply = row.getCell(4).getStringCellValue();
                    String holdPercentage = row.getCell(5).getStringCellValue();
                    String ath = row.getCell(6).getStringCellValue();
                    String duration = row.getCell(7).getStringCellValue();

                    // Display or process existing wallets as needed
                    System.out.println("Existing Wallet: " + address + " - " + tokenName);
                }
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWallet() {
        // Retrieve values from text fields
        String address = addressField.getText();
        String tokenName = tokenNameField.getText();
        String mintTime = mintTimeField.getText();
        String deployTime = deployTimeField.getText();
        String supply = supplyField.getText();
        String holdPercentage = holdPercentageField.getText();
        String ath = athField.getText();
        String duration = durationField.getText();

        // Create data row in Excel sheet
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(address);
        row.createCell(1).setCellValue(tokenName);
        row.createCell(2).setCellValue(mintTime);
        row.createCell(3).setCellValue(deployTime);
        row.createCell(4).setCellValue(supply);
        row.createCell(5).setCellValue(holdPercentage);
        row.createCell(6).setCellValue(ath);
        row.createCell(7).setCellValue(duration);

        // Write to Excel file
        writeToExcel();

        // Clear fields after adding
        clearFields();
    }

    private void writeToExcel() {
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            outputStream.close();
            System.out.println("Excel written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        addressField.setText("");
        tokenNameField.setText("");
        mintTimeField.setText("");
        deployTimeField.setText("");
        supplyField.setText("");
        holdPercentageField.setText("");
        athField.setText("");
        durationField.setText("");
    }

    public static void main(String[] args) {
        // Use Event Dispatch Thread (EDT) for Swing applications
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WalletApp();
            }
        });
    }
}
