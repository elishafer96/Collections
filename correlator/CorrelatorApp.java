/*
 * Eli Shafer
 * TCSS 342 - Assignment 4
 */

package correlator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CorrelatorApp {

	
	/**
	 * Determines the jaccard index of hamlet.txt and the-new-atlantis.txt.
	 * 
	 * @param args command arguments
	 */
	public static void main(String[] args) {
		HashMap<String, Integer> hamletMap = new HashMap<>();
		HashMap<String, Integer> atlantisMap = new HashMap<>();
		
		Scanner hamlet = null, newAtlantis = null;
		try {
			hamlet = new Scanner(new File("hamlet.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not open hamlet.txt.");
			e.printStackTrace();
		}
		try {
			newAtlantis = new Scanner(new File("the-new-atlantis.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not open the-new-atlantis.txt.");
			e.printStackTrace();
		}
		
		String currentWord;
		Integer numTimes;
		int numInsertions = 0;
		
		while ((currentWord = CorrelatorApp.getWord(hamlet)) != null) {
			numTimes = hamletMap.get(currentWord);
			if(numTimes == null)
				numTimes = 0;
			numTimes++;
			hamletMap.put(currentWord, numTimes);
			numInsertions++;
		}
		System.out.println("Hamlet map: " + numInsertions + " insertions.");
		
		numInsertions = 0;
		while ((currentWord = CorrelatorApp.getWord(newAtlantis)) != null) {
			numTimes = atlantisMap.get(currentWord);
			if(numTimes == null)
				numTimes = 0;
			numTimes++;
			atlantisMap.put(currentWord, numTimes);
			numInsertions++;
		}
		System.out.println("The New Atlantis map: " + numInsertions + " insertions.");
		
		Heap<HeapEntry<String, Integer>> hamletHeap = new Heap<>(new HeapEntry<>());
		Heap<HeapEntry<String, Integer>> atlantisHeap = new Heap<>(new HeapEntry<>());
		
		
		for (String key : hamletMap.keySet()) {
			hamletHeap.add(new HeapEntry<String, Integer>(key, hamletMap.get(key)));
		}
		for (String key : atlantisMap.keySet()) {
			atlantisHeap.add(new HeapEntry<String, Integer>(key, atlantisMap.get(key)));
		}
		
		System.out.println("\nHamlet writer's signature:");
		for (int i = 0; i < 10; i++) {
			HeapEntry<String, Integer> entry = hamletHeap.remove();
			System.out.print(entry.toString());
			System.out.println(". Frequency: " + (double)(entry.getValue()) / hamletMap.size());
		}
		
		System.out.println("\nThe New Atlantis writer's signature:");
		for (int i = 0; i < 10; i++) {
			HeapEntry<String, Integer> entry = atlantisHeap.remove();
			System.out.print(entry.toString());
			System.out.println(". Frequency: " + (double)(entry.getValue()) / atlantisMap.size());
		}
		
		System.out.println("\nDocument correlation:");
		
		
		HashMap<String, Integer> hamletNorm = new HashMap<>();
		for (String key : hamletMap.keySet()) {
			Integer count = hamletMap.get(key);
			double frequency = ((double) count / (double) hamletMap.size());
			if (frequency < 0.01 && frequency > 0.0001)
				hamletNorm.put(key, count);
		}
		
		HashMap<String, Integer> atlantisNorm = new HashMap<>();
		for (String key : atlantisMap.keySet()) {
			Integer count = atlantisMap.get(key);
			double frequency = ((double) count / (double) hamletMap.size());
			if (frequency < 0.01 && frequency > 0.0001)
				atlantisNorm.put(key, count);
		}

		int intersection = hamletNorm.intersection(atlantisNorm);
		int union = hamletNorm.union(atlantisNorm);
		
		System.out.println("Intersection size: " + intersection);
		System.out.println("Union size: " + union);
		System.out.println("Jaccard index: " + (double) intersection / union);
	}
	
	/**
     * Return the next word in a file.
     * @param file  the file to read from
     * @return  the next word in the file of length >= 3, or null if there is no next word
     * A word is defined here as a consecutive sequence of alphanumeric characters.
     */
    private static String getWord(Scanner file) {
       String word = null;
       while (file.hasNext()) {
           word = file.next();
           word = word.replaceAll("\\W", "").toLowerCase();
           if (word.length() >= 3)
               break;
       }
       if (word != null && word.length() < 3 ) word = null;
       return word;
       
    }

}
