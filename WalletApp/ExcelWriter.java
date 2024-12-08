package com.example.wallet;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExcelWriter {

    private static final String FILE_NAME = "wallets.xlsx";

    public static void writeToExcel(List<Wallet> wallets) {
        Workbook workbook = null;
        Sheet sheet;

        // Check if file exists
        Path filePath = Paths.get(FILE_NAME);
        boolean fileExists = Files.exists(filePath);

        if (fileExists) {
            // If file exists, open existing workbook
            try {
                workbook = WorkbookFactory.create(filePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If file doesn't exist, create new workbook
            workbook = new XSSFWorkbook();
        }

        // Create or get the sheet
        sheet = workbook.getSheet("Wallets");
        if (sheet == null) {
            sheet = workbook.createSheet("Wallets");
        }

        // Write wallets to the sheet
        int rowCount = sheet.getLastRowNum() + 1;

        for (Wallet wallet : wallets) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(wallet.getAddress());
            row.createCell(1).setCellValue(wallet.getTokenName());
            row.createCell(2).setCellValue(wallet.getMintTime());
            row.createCell(3).setCellValue(wallet.getDeployTime());
            row.createCell(4).setCellValue(wallet.getSupply());
            row.createCell(5).setCellValue(wallet.getHoldPercentage());
            row.createCell(6).setCellValue(wallet.getAth());
            row.createCell(7).setCellValue(wallet.getDuration());
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
