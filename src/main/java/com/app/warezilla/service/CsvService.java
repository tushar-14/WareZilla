package com.app.warezilla.service;

import com.app.warezilla.model.Transaction;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CsvService {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Write transactions as CSV to the provided OutputStream (UTF-8).
     * Header: id,item,quantity,type,person,timestamp,notes,userId
     */
    public void exportTransactionsToCsv(List<Transaction> transactions, OutputStream out) throws IOException {
        byte[] csvBytes = transactionsToCsvBytes(transactions);
        out.write(csvBytes);
        out.flush();
    }

    /**
     * Return CSV bytes (UTF-8) for the provided transactions.
     */
    public byte[] transactionsToCsvBytes(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        sb.append("ITEM,QUANTITY,TYPE,PERSON,TIME,NOTES").append('\n');

        for (Transaction t : transactions) {
            sb.append(escapeCsv(t.getItem())).append(',');
            sb.append(escapeCsv(String.valueOf(t.getQuantity()))).append(',');
            sb.append(escapeCsv(t.getType() == null ? "" : t.getType().name())).append(',');
            sb.append(escapeCsv(t.getPerson())).append(',');
            sb.append(escapeCsv(t.getTimestamp().format(TIMESTAMP_FORMATTER))).append(',');
            sb.append(escapeCsv(t.getNotes())).append(',');
            sb.append('\n');
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Minimal CSV field escaping: double quotes are doubled; field wrapped in quotes if needed.
     */
    private String escapeCsv(String field) {
        if (field == null) return "";
        boolean needQuotes = field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r");
        String escaped = field.replace("\"", "\"\"");
        return needQuotes ? "\"" + escaped + "\"" : escaped;
    }
}
