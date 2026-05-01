package com.wedding.util;

import com.wedding.model.Client;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    // Defines the file path - Using a specific path within user directory to avoid permission issues
    private static final String FILE_PATH = System.getProperty("user.home") + File.separator + "clients.txt";

    public static void saveClient(Client client) throws IOException {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(client.toString());
        }
    }

    public static List<Client> getAllClients() throws IOException {
        List<Client> clients = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return clients;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Ensure we have correct number of attributes
                if (data.length >= 10) {
                    Client client = new Client(data[0], data[1], data[2], data[3], data[4], data[5], Double.parseDouble(data[6]), data[7], data[8], Integer.parseInt(data[9]));
                    clients.add(client);
                } else if (data.length >= 7) {
                    // Fallback for old data
                    Client client = new Client(data[0], data[1], data[2], data[3], data[4], data[5], Double.parseDouble(data[6]), "N/A", "N/A", 0);
                    clients.add(client);
                }
            }
        }
        return clients;
    }

    public static Client getClientById(String userId) throws IOException {
        List<Client> clients = getAllClients();
        for (Client client : clients) {
            if (client.getUserId().equals(userId)) {
                return client;
            }
        }
        return null;
    }

    public static Client authenticate(String email, String password) throws IOException {
        List<Client> clients = getAllClients();
        for (Client client : clients) {
            if (client.getEmail().equals(email) && client.getPassword().equals(password)) {
                return client;
            }
        }
        return null;
    }

    public static void updateClient(Client updatedClient) throws IOException {
        List<Client> clients = getAllClients();
        try (FileWriter fw = new FileWriter(FILE_PATH, false); // Overwrite file
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (Client client : clients) {
                if (client.getUserId().equals(updatedClient.getUserId())) {
                    out.println(updatedClient.toString());
                } else {
                    out.println(client.toString());
                }
            }
        }
    }

    public static void deleteClient(String userId) throws IOException {
        List<Client> clients = getAllClients();
        try (FileWriter fw = new FileWriter(FILE_PATH, false); // Overwrite file
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (Client client : clients) {
                // If not the user we want to delete, write it back
                if (!client.getUserId().equals(userId)) {
                    out.println(client.toString());
                }
            }
        }
    }
}
