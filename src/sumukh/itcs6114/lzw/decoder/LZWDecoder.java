package sumukh.itcs6114.lzw.decoder;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Hashtable;

public class LZWDecoder {
	
	private static int tableMaxSize;
	private static String fileName;

	public static void main(String[] args) {
		fileName = args[0];
		String inputFileName = (System.getProperty("user.dir")).toString() + "\\" + args[0];
		System.out.println(inputFileName);
		int bitLength = Integer.parseInt(args[1]);
		tableMaxSize = (int) Math.pow(2, bitLength);
		Hashtable<Integer, String> table = new Hashtable<>(tableMaxSize);
		for (int i = 0; i < 256; i++) {
			String value = String.valueOf((char) i); 
			table.put(i, value);
		}
		decodeLZW(inputFileName, table);
	}

	private static void decodeLZW(String inputFileName, Hashtable<Integer, String> table) {
		try {
			FileInputStream fileInputStream = new FileInputStream(inputFileName);
			InputStreamReader reader = new InputStreamReader(fileInputStream, "UTF-16BE");
			doLZWDecoding(reader, table);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	private static void doLZWDecoding(InputStreamReader reader, Hashtable<Integer, String> table) throws IOException {
		String outputFileName = fileName.substring(0, fileName.length() - 4);
		outputFileName = (System.getProperty("user.dir")).toString() + "\\" + outputFileName + "_decoded" + ".txt";
		Writer outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));
		int keys = 255;
		int code = reader.read();
		String string = table.get(code);
		outWriter.write(string);
		outWriter.flush();
		while ((code = reader.read()) != -1) {
			String newString = "";
			if (!table.containsKey(code)) {
				newString = newString.concat(string + string.charAt(0));
			}
			else {
				newString = table.get(code);
			}
			outWriter.write(newString);
			outWriter.flush();
			if (table.size() < tableMaxSize) {
				table.put(++keys, string.concat(String.valueOf(newString.charAt(0))));
			}
			string = newString;
		}
		outWriter.close();
	}

}
