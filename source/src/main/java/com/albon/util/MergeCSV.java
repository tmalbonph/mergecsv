/*
 * Copyright (c) 2019 Teddy Albon Sr.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.albon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <p>
 * This MergeCSV class defines main class for this mergecsv application.
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>This class is mutable thus not thread safe
 * but it is use in thread safe manner.
 * </p>
 *
 * @author Teddy Albon Sr &lt;tmalbonph@yahoo.com&gt;
 * @version 1.0
 */
public class MergeCSV {

	/**
	 * Input A, a CSV file.
	 */
	private BufferedReader brAInput;

	/**
	 * Input B, a CSV file.
	 */
	private BufferedReader brBInput;

	/**
	 * Template, a CSV file.
	 */
	private BufferedReader brTemplate;

	/**
	 * Generated CSV file.
	 */
	private BufferedWriter bwOutput;

	/**
	 * Default constructor.
	 */
	public MergeCSV() {
		brAInput = null;
		brBInput = null;
		brTemplate = null;
		bwOutput = null;
	}

	/**
	 * Read line from BufferedReader.
	 *
	 * @param br BufferedReader instance
	 * @return String
	 */
	private String readLine(BufferedReader br) {
		String line;
		try {
			line = br.readLine();
		} catch (IOException e) {
			line = null;
		}
		return line;
	}

	/**
	 * Parse comma delimited line.
	 *
	 * @param line String
	 * @param indexes Int
	 * @return String
	 */
	private String getData(final String line, int[] indexes) {
		final char cDoubleQoute = '"';
		char c;
		int offset = indexes[0];
		int limit = line.length();
		boolean inLiteral = false;
		String s = "";
		if (offset < limit) {
			if (cDoubleQoute == line.charAt(offset)) {
				inLiteral = true;
				offset = offset + 1;
				s = s + cDoubleQoute;
				// Check two-double-quote
				if (offset < limit) {
					c = line.charAt(offset);
					if (c == cDoubleQoute) {
						s = s + cDoubleQoute;
						offset = offset + 1;
						inLiteral = false;
					}
				}
			}
		}
		for ( ; offset < limit; ) {

			c = line.charAt(offset);
			offset = offset + 1;

			if (c == ',' && inLiteral) {
				s = s + c;
			}
			else {
				// Check for two-double-quote
				if (c == cDoubleQoute
				&& ((offset < limit)
				&& cDoubleQoute == line.charAt(offset))
				) {
					s = s + "\"\"";
					offset = offset + 1;
					continue;
				}
				else
				if ((c == cDoubleQoute)
				|| (c == ',')
				) {
					if (c == cDoubleQoute) {
						s = s + c;
						offset = offset + 1;
					}

					while (offset < limit) {
						c = line.charAt(offset);
						if (c == ' '
						|| c == '\t'
						) {
							offset = offset + 1;
						}
						else {
							break;
						}
					}

					break;
				}
				s = s + c;
			}
		}
		indexes[0] = offset;

		// Fix in literal delimiter
		String p;
		if (inLiteral) {
			limit = s.length() - 1;
			if (limit < 1) {
				return "";
			}
			if (s.charAt(limit) == cDoubleQoute) {
				return s;
			}
			p = s + cDoubleQoute;
			return p;
		}

		// Fix string with embedded space
		p = s.trim();
		limit = p.length();
		if (limit > 0) {
			offset = p.indexOf(' ');
			if (offset > 0) {
				return "\"" + p + "\"";
			}

			return p;
		}

		return "";
	}

	/**
	 * Parse a comma delimited CSV file.
	 *
	 * @param line String
	 *
	 * @return Array of String or null
	 */
	private String[] getCsvData(final String line) {

		String s;
		int index, limit;
		int size, offset, counter;
		int[] indexes = {0,0};

		String[] datas;

		// Count how many data on this line.
		limit = line.length();
		size = 0;
		index = 0;
		do {
			indexes[0] = index;
			s = getData(line, indexes);
			offset = indexes[0];
			if (offset == index) {
				break;
			}
			size = size + 1;
			index = offset;
		} while (offset < limit);

		if (size < 2) {
			return null;
		}

		// Allocate the String
		datas = new String[size];
		index = 0;
		counter = 0;
		do {
			indexes[0] = index;
			s = getData(line, indexes);
			datas[counter] = s;
			offset = indexes[0];
			if (offset == index) {
				break;
			}
			counter = counter + 1;
			index = offset;
		} while (offset < limit);

		while (counter < size) {
			datas[counter] = "";
			counter = counter + 1;
		}
		return datas;
	}

	/**
	 * Read the CSV header information.
	 *
	 * @param br BufferedReader
	 * @param fileName String
	 * @return Array of String
	 *
	 * @throws MergeCsvException If no valid CSV header
	 */
	private String[] readHeader(BufferedReader br,
			final String fileName
	) throws MergeCsvException {

		String[] headers;

		String line = readLine(br);
		// minimum value "a,b"
		if (line == null || line.length() < 3) {
			throw new MergeCsvException("Missing header data on '"+ fileName +"'");
		}

		headers = getCsvData(line);
		// minumin value is 2
		if (headers == null || headers.length < 2) {
			throw new MergeCsvException("Missing header data on '"+ fileName +"'");
		}

		return headers;
	}

	/**
	 * Locate key index.
	 *
	 * @param key String
	 * @param headers Array of String
	 * @return int 0 if key not in headers
	 */
	private int locateKey(final String key, final String[] headers) {

		int index;
		for (index = 0; index < headers.length; index++) {
			if (key.equals(headers[index])) {
				return index + 1;
			}
		}

		return 0;
	}

	/**
	 * Helper to show parsed Header for debugging.
	 *
	 * @param headers Array of String headers
	 * @param name String header name
	 */
	void emitHeader(final String[] headers, final String name) {
		String s = "";
		int index;
		for (index = 0; index < headers.length; index++) {
			if (index > 0) {
				s = s + ",";
			}
			s = s + headers[index];
		}
		log("Header from: "+ name + " - ["+ s +"]");
	}

	/**
	 * Process CSV files.
	 *
	 * @param aInput String file 1
	 * @param bInput String file 2
	 * @param output String file 4
	 * @param template String file 3
	 *
	 * @throws MergeCsvException If there is error at File I/O level
	 */
	void run(final String aInput,
			final String bInput,
			final String output,
			final String template
	) throws MergeCsvException {

		String fileName;
		String forOutputHeader;
		String line1;
		String line2;
		String key;
		String s;
		String xs;
		String[] aHeader;
		String[] bHeader;
		String[] outputHeader;
		String[] aData;
		String[] bData;
		int outputSize;
		int index;
		int offset;
		int xOffset;
		int[] aIndexes;
		int[] bIndexes;
		long counters;
		long ignored;
		// 1. Load the template
		fileName = template;
		try {
			brTemplate = Helper.createFileReader(null, fileName);

			// 2. read template header information
			forOutputHeader = readLine(brTemplate);
			if (forOutputHeader == null || forOutputHeader.length() < 3) {
				throw new MergeCsvException("Missing header data on '"+ template +"'");
			}

			outputHeader = getCsvData(forOutputHeader);
			if (outputHeader == null || outputHeader.length < 2) {
				throw new MergeCsvException("Missing header data on '"+ template +"'");
			}
			Helper.closeReader(brTemplate);
			brTemplate = null;

			log("Template '"+ template +"' contains "+ outputHeader.length + " columns");
			//emitHeader(outputHeader, "Template "+ template);

			// 3. Load input file #1
			fileName = aInput;
			brAInput = Helper.createFileReader(null, fileName);

			// 4. read input file #1 header information
			aHeader = readHeader(brAInput, aInput);
			log("Input 1 '"+ aInput + "' contains "+ aHeader.length + " columns");
			//emitHeader(aHeader, aInput);

			// 5. Load input file #2
			fileName = bInput;
			brBInput = Helper.createFileReader(null, fileName);

			// 6. read input file #2 header information
			bHeader = readHeader(brBInput, bInput);
			log("Input 2 '"+ bInput + "' contains "+ bHeader.length + " columns");
			//emitHeader(bHeader, bInput);

		} catch (FileNotFoundException ex) {
			throw new MergeCsvException("Not found '"+ fileName +"'", ex);
		}

		// 7. Create the output CSV file
		log("Creating output '"+ output + "'");
		bwOutput = Helper.createBufferedWriter(output, "run");

		try {
			// 8. Write the ouput CSV header.
			bwOutput.write(forOutputHeader + "\n");
			bwOutput.flush();

		} catch (IOException e) {
			throw new MergeCsvException("Error writing to "+ output, e);
		}

		// 9. Create a mapping
		outputSize = outputHeader.length;

		aIndexes = new int[outputSize];
		bIndexes = new int[outputSize];
		for (index = 0; index < outputSize; index++) {
			key = outputHeader[index];
			// Mapping for first CSV
			offset = locateKey(key, aHeader);
			aIndexes[index] = offset;
			// Mapping for 2nd CSV
			xOffset = locateKey(key, bHeader);
			bIndexes[index] = xOffset;
		}

		counters = 0L;
		ignored = 0L;
		do {
			// 10. read both CSV
			line1 = readLine(brAInput);
			if (line1 == null || line1.length() < 2) {
				break;
			}
			aData = getCsvData(line1);
			if (aData == null || aData.length < 2) {
				break;
			}

			line2 = readLine(brBInput);
			if (line2 == null || line2.length() < 2) {
				break;
			}
			bData = getCsvData(line2);
			if (bData == null || bData.length < 2) {
				break;
			}

			counters = counters + 1L;
			if ((counters % 1024L) == 0L) {
				log("Merging "+ counters +" CSV records");
			}

			if ((aData.length >= aHeader.length)
			&& (bData.length >= bHeader.length)
			) {
				// 11. Merge the CSV
				s = "";
				for (index = 0; index < outputSize; index++) {
					if (index > 0) {
						s = s + ",";
					}
					offset = aIndexes[index];
					xOffset = bIndexes[index];
					if (offset == 0 && xOffset == 0) {
						// key not in 1st and 2nd CSV
					}
					else if (xOffset == 0 && offset > 0) {
						// key at 1st CSV
						s = s + aData[offset - 1];
					}
					else if (xOffset > 0 && offset == 0) {
						// key at 2nd CSV
						s = s + bData[xOffset - 1];
					}
					else {
						// key both at 1st and 2nd CSV
						key = aData[offset - 1];
						xs = bData[xOffset - 1];
						if (key.length() < 1) {
							s = s + xs;
						}
						else if (xs.length() < 1) {
							s = s + key;
						}
						else if (key.length() >= xs.length()) {
							s = s + key;
						}
						else {
							s = s + xs;
						}
					}
				}
				s = s + "\n";

				try {
					bwOutput.write(s);
					bwOutput.flush();
				} catch (IOException e) {
					throw new MergeCsvException("Error writing to "+ output, e);
				}
			}
			else {
				ignored++;
			}
		} while(true);

		log("Merged "+ counters +" CSV records, Ignored "+ ignored +" CSV record(s).");
	}

	/**
	 * MergerCSV start here.
	 *
	 * @param args Array of String parameters
	 */
	public static void main(String[] args) {
		MergeCSV app;
		String pzAInput = null;
		String pzBInput = null;
		String pzOut = null;
		String pzPlate = null;
		String par;
		int index;
		int length = args.length;
		for (index = 0; index < length; ) {
			par = args[index++];
			if (index == length) {
				missingParameter(par);
				return;
			}
			if ("--a".equals(par)) {
				pzAInput = args[index++];
			}
			else if ("--b".equals(par)) {
				pzBInput = args[index++];
			}
			else if ("--o".equals(par)) {
				pzOut = args[index++];
			}
			else if ("--t".equals(par)) {
				pzPlate = args[index++];
			} else {
				quit(1, "Unknown parameter '"+ par +"'");
				return;
			}
		}
		if (Helper.isStringNullOrEmpty(pzAInput)) {
			missingParameter("--a");
		}
		if (Helper.isStringNullOrEmpty(pzBInput)) {
			missingParameter("--b");
		}
		if (Helper.isStringNullOrEmpty(pzOut)) {
			missingParameter("--o");
		}
		if (Helper.isStringNullOrEmpty(pzPlate)) {
			missingParameter("--t");
		}

		app = new MergeCSV();
		try {
			app.run(pzAInput, pzBInput, pzOut, pzPlate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		app.close();
		app = null;
		quit(0, null);
	}

	/**
	 * How To messages.
	 */
	private static final String[] howTo = {
		"",
		"MergeCSV version 1.0, (c) 2019 tmalbonph@yahoo.com",
		"",
		"usage: com.albon.util.MergeCSV --a A --b B --t T --o O",
		"where: --o O specify {O} to save merge CSV column into this {O} file",
		"       --a A specify {A} as 1st input CSV file to merge into {O} file",
		"       --b B specify {B} as 2nd input CSV file to merge into {O} file",
		"       --t T specify {T} as the Template that specify the CSV column header,",
		"             that exist on file {A} or {B} and written into {O} CSV file.",
		""
	};

	/**
	 * Helper method that display how to use this java application.
	 */
	private static void usage() {
		int index;
		for (index = 0; index < howTo.length; index++) {
			System.err.println(howTo[index]);
		}
	}

	/**
	 * Display Missing parameter error and quit.
	 * @param par String parameter name
	 */
	private static void missingParameter(final String par) {
		usage();
		quit(1, "Missing parameter for '"+ par +"'");
	}

	/**
	 * Free file resources.
	 */
	private void close() {
		Helper.closeReader(brAInput);
		brAInput = null;
		Helper.closeReader(brBInput);
		brBInput = null;
		Helper.closeReader(brTemplate);
		brTemplate = null;
		Helper.closeWriter(bwOutput);
		bwOutput = null;
	}

	/**
	 * MergeCSV ends here.
	 *
	 * @param err int
	 * @param msg String
	 */
	private static void quit(int err, final String msg) {
		if (msg != null) {
			System.err.println("ERROR: " + msg);
		}
		System.exit(err);
	}

	/**
	 * Log error messages into the console only.
	 * @param msg String message to log
	 */
	private static void log(String msg) {
		System.err.println(msg);
	}

}
