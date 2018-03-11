package sumukh.itcs6114.lzw.encoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Hashtable;

public class LZWEncoder {

	private static int tableMaxSize;
	private static String fileName;

	public static void main(String[] args) {
		fileName = args[0];
		String inputFileName = (System.getProperty("user.dir")).toString() + "\\" + args[0];
		System.out.println(inputFileName);
		int bitLength = Integer.parseInt(args[1]);
		tableMaxSize = (int) Math.pow(2, bitLength);
		Hashtable<String, Integer> table = new Hashtable<>(tableMaxSize);
		for (int i = 0; i < 256; i++) {
			String key = String.valueOf((char) i);
			table.put(key, i);
		}
		encodeLZW(inputFileName, table);
	}

	private static void encodeLZW(String inputFileName, Hashtable<String, Integer> table) {
		File inputFile = new File(inputFileName);
		doInitialFileOperations(inputFile, table);
	}

	private static void doInitialFileOperations(File inputFile, Hashtable<String, Integer> table) {
		try {
			FileInputStream inputStream = new FileInputStream(inputFile);
			doLZWEncoding(inputStream, table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doLZWEncoding(FileInputStream inputStream, Hashtable<String, Integer> table)
			throws IOException {
		String outputFileName = fileName.substring(0, fileName.length() - 4);
		outputFileName = (System.getProperty("user.dir")).toString() + "\\" + outputFileName + ".lzw";
		Writer outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-16BE"));
		int r = -1;
		String string = "";
		int value = 256;
		while ((r = inputStream.read()) != -1) {
			if (table.containsKey(string + String.valueOf((char) r))) {
				string = string.concat(String.valueOf((char) r));
			} else {
				outWriter.write(table.get(string));
				outWriter.flush();
				if (table.size() < tableMaxSize) {
					string = string.concat(String.valueOf((char) r));
					table.put(string, value++);
					string = String.valueOf((char) r);
				} else {
					System.out.println("Table limit reached! Exiting!");
					outWriter.close();
					return;
				}
			}

		}
		outWriter.write(table.get(string));
		outWriter.flush();
		outWriter.close();
	}
}
