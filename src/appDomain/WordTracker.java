package appDomain;

import implementations.BSTree;
import utilities.Iterator;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WordTracker {
    private static final String REPOSITORY_FILE = "repository.ser";
    private BSTree<WordRecord> wordTree;

    public WordTracker() {
        this.wordTree = new BSTree<>();
    }

    public static void main(String[] args) {
        WordTracker tracker = new WordTracker();
        
        
        if (args.length < 2) {
            System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f<output.txt>]");
            return;
        }

        String inputFileName = args[0];
        String command = args[1];
        String outputFileName = null;
        
        if (args.length > 2 && args[2].startsWith("-f")) {
            outputFileName = args[2].substring(2);
        }

        
        tracker.loadRepository();

        
        tracker.processFile(inputFileName);

        
        String report = tracker.generateReport(command);
        
        
        if (outputFileName != null) {
            try (PrintWriter out = new PrintWriter(new FileWriter(outputFileName))) {
                out.print(report);
                System.out.println("Report saved to " + outputFileName);
            } catch (IOException e) {
                System.err.println("Error writing to output file: " + e.getMessage());
            }
        } else {
            System.out.println(report);
        }

        
        tracker.saveRepository();
    }

    @SuppressWarnings("unchecked")
    private void loadRepository() {
        File file = new File(REPOSITORY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                wordTree = (BSTree<WordRecord>) ois.readObject();
                
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading repository: " + e.getMessage());
                
                wordTree = new BSTree<>();
            }
        }
    }

    private void saveRepository() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE))) {
            oos.writeObject(wordTree);
            
        } catch (IOException e) {
            System.err.println("Error saving repository: " + e.getMessage());
        }
    }

    private void processFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("Input file not found: " + filename);
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();
                
                String[] words = line.split("[^a-zA-Z0-9']+");
                
                for (String w : words) {
                    if (w.isEmpty()) continue;
                    
                    
                    WordRecord temp = new WordRecord(w);
                    WordRecord existing = wordTree.retrieve(temp); 
                    
                    if (existing != null) {
                        existing.addLocation(filename, lineNumber);
                    } else {
                        
                        temp.addLocation(filename, lineNumber);
                        wordTree.add(temp);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String generateReport(String command) {
        StringBuilder sb = new StringBuilder();
        Iterator<WordRecord> it = wordTree.inorderIterator();
        
        while (it.hasNext()) {
            WordRecord r = it.next();
            
            
            sb.append("Key:===").append(r.getWord()).append("===");
            
            
            if (command.equals("-pf")) {
                sb.append(" found in files: ");
                for (String file : r.getFileAppearances().keySet()) {
                    sb.append(file).append(" ");
                }
            }
            
            else if (command.equals("-pl")) {
                sb.append(" found in files: ");
                r.getFileAppearances().forEach((file, lines) -> {
                    sb.append(file).append(" on lines: ");
                    for (int line : lines) sb.append(line).append(",");
                    sb.append(" ");
                });
            }
            
            else if (command.equals("-po")) {
                sb.append(" number of entries: ").append(r.getTotalFrequency());
                sb.append(" found in files: ");
                r.getFileAppearances().forEach((file, lines) -> {
                    sb.append(file).append(" on lines: ");
                    for (int line : lines) sb.append(line).append(",");
                    sb.append(" ");
                });
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}