package partA;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedFile {

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private static File file = new File("");

    public static String findPhoneByLastName(String lastName) throws IOException {
        lock.readLock().lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts[0].equals(lastName)) {
                    return parts[1];
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return ""; // Default value if the record is not found
    }

    public static String findLastNameByPhoneNumber(String phoneNumber) throws IOException {
        lock.readLock().lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts[1].equals(phoneNumber)) {
                    return parts[0];
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return ""; // Default value if the record is not found
    }

    public static void addRecord(String name, String phoneNumber) throws IOException {
        lock.writeLock().lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(name + " - " + phoneNumber);
            writer.newLine();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void deleteRecord(String name) throws IOException {
        lock.writeLock().lock();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            FileWriter writer = new FileWriter(file, false); // Overwrite the file

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (!parts[0].equals(name)) {
                    writer.write(line);
                    writer.write("\n");
                }
            }

            reader.close();
            writer.close();
        } finally {
            lock.writeLock().unlock();
        }

        // Check if the record was deleted
        String phone = findPhoneByLastName(name);
        if (phone.isEmpty()) {
            System.out.println("Record for " + name + " deleted successfully.");
        } else {
            System.out.println("Record for " + name + " could not be deleted.");
        }
    }

    public static void main(String[] args) throws IOException {
        // Find the phone number for "John Doe"
        String phoneNumber = findPhoneByLastName("Doe");
        if (phoneNumber.isEmpty()) {
            System.out.println("Record for John Doe not found.");
        } else {
            System.out.println("Phone number for John Doe: " + phoneNumber);
        }

        // Find the last name for the phone number "123-456-7890"
        String lastName = findLastNameByPhoneNumber("123-456-7890");
        if (lastName.isEmpty()) {
            System.out.println("Record for phone number 123-456-7890 not found.");
        } else {
            System.out.println("Last name for 123-456-7890: " + lastName);
        }

        // Add a new record to the file
        addRecord("Jane Doe", "987-654-3210");

        // Delete the record for "Jane Doe"
        deleteRecord("Jane Doe");
    }
}
