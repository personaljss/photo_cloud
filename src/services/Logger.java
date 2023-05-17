package services;

import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	//Paths of the files to be logged
	private static String infoPath="logs/info.txt";
	private static String errorPath="logs/errors.txt";
	//singleton structure

	
	public static void logError(String error) {
		try (PrintWriter out = new PrintWriter(errorPath)) {
            out.println(error);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
	}
	
	public static void logInfo(String info) {
		try (PrintWriter out = new PrintWriter(infoPath)) {
            out.println(info);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
	}
	
}
