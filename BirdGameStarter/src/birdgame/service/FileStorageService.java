package birdgame.service;

import birdgame.model.PlayerRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path HISTORY_FILE = DATA_DIR.resolve("player_history.csv");

    public FileStorageService() {
        try {
            Files.createDirectories(DATA_DIR);

            if (!Files.exists(HISTORY_FILE)) {
                Files.writeString(
                    HISTORY_FILE,
                    "playerName,finished,highestLevel,totalPipes,playedAt\n",
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize player history storage.", e);
        }
    }

    public void saveRecord(PlayerRecord record) {
        String line = csv(record.getPlayerName()) + ","
                + record.isFinished() + ","
                + record.getHighestLevel() + ","
                + record.getTotalPipes() + ","
                + csv(record.getPlayedAt())
                + System.lineSeparator();

        try {
            Files.writeString(
                HISTORY_FILE,
                line,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Cannot save player history.", e);
        }
    }

    public List<PlayerRecord> loadRecords() {
        List<PlayerRecord> records = new ArrayList<>();

        if (!Files.exists(HISTORY_FILE)) {
            return records;
        }

        try {
            List<String> lines = Files.readAllLines(HISTORY_FILE, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();

                if (line.isBlank()) {
                    continue;
                }

                List<String> cols = parseCsvLine(line);

                if (cols.size() < 5) {
                    continue;
                }

                records.add(new PlayerRecord(
                    cols.get(0),
                    Boolean.parseBoolean(cols.get(1)),
                    Integer.parseInt(cols.get(2)),
                    Integer.parseInt(cols.get(3)),
                    cols.get(4)
                ));
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Cannot load player history.", e);
        }

        return records;
    }

    private String csv(String value) {
        if (value == null) {
            return "\"\"";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result;
    }
}
