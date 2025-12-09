package appDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class WordRecord implements Comparable<WordRecord>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String word;
    // Map: Filename -> List of line numbers
    private HashMap<String, ArrayList<Integer>> fileAppearances;
    private int totalFrequency;

    public WordRecord(String word) {
        this.word = word;
        this.fileAppearances = new HashMap<>();
        this.totalFrequency = 0;
    }

    public void addLocation(String filename, int lineNumber) {
        if (!fileAppearances.containsKey(filename)) {
            fileAppearances.put(filename, new ArrayList<>());
        }
        // 记录行号
        fileAppearances.get(filename).add(lineNumber);
        totalFrequency++;
    }

    public String getWord() {
        return word;
    }
    
    public int getTotalFrequency() {
        return totalFrequency;
    }
    
    public HashMap<String, ArrayList<Integer>> getFileAppearances() {
        return fileAppearances;
    }

    @Override
    public int compareTo(WordRecord other) {
        // 按照单词字母顺序排序 (Case insensitive usually preferred, but using standard string compare)
        return this.word.compareToIgnoreCase(other.word);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WordRecord that = (WordRecord) obj;
        return word.equalsIgnoreCase(that.word);
    }
    
    @Override
    public String toString() {
        return "Key: ===" + word + "===";
    }
}