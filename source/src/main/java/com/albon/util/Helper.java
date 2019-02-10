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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>
 * This Helper class defines common methods for this mergecsv application.
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>This class is not mutable thus thread safe.
 * </p>
 *
 * @author Teddy Albon Sr &lt;tmalbonph@yahoo.com&gt;
 * @version 1.0
 */
public final class Helper {
	/**
	 * Buffering size for creating BufferedWriter instance.
	 */
	private static final int BUFFERED_BUFFER_SIZE = 16 * 65536;

	/**
	 * Check if the given Object is null.
	 *
	 * @param param Object
	 * @param name String name of the Object
	 * @throws IllegalArgumentException
	 *   Is thrown if the given Object is null.
	 */
	public static void checkObject(Object param, final String name
	) throws IllegalArgumentException {
		if (param == null) {
			throw new IllegalArgumentException("Parameter '"
				+ name +"' cannot be null.");
		}
	}

	/**
	 * Check if given String is null or trim empty.
	 *
	 * @param param String 
	 * @return Boolean true if given String is null or trim empty.
	 */
	public static boolean isStringNullOrEmpty(String param) {
		if (param != null) {
			String s = param.trim();
			if (s.length() > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if the given String is null or trim empty.
	 *
	 * @param param String
	 * @param name String name of the parameter
	 * @throws IllegalArgumentException
	 *   Is thrown if the given String is null or trim empty.
	 */
	public static void checkStringNullOrEmpty(String param,
		final String name
	) throws IllegalArgumentException {
		checkObject(param, name);
		if (isStringNullOrEmpty(param)) {
			throw new IllegalArgumentException("Parameter '"
				+ name +"' cannot be empty.");
		}
	}

	/**
	 * Convert a given String into an Integer.
	 *
	 * @param param String
	 * @return Integer 0 if given String is not a number
	 */
	public static int atoi(final String param) {
		int value = 0;
		if (!isStringNullOrEmpty(param)) {
			try {
				value = Integer.parseInt(param, 10);
			} catch (NumberFormatException ignore) {
				value = 0;
			}
		}
		return value;
	}

	/**
	 * Convert a given String into a Long Integer.
	 *
	 * @param param String
	 * @return Long 0 if given String is not a number
	 */
	public static long atol(final String param) {
		long value = 0L;
		if (!isStringNullOrEmpty(param)) {
			try {
				value = Long.parseLong(param, 10);
			} catch (NumberFormatException ignore) {
				value = 0L;
			}
		}
		return value;
	}

	/**
	 * Convert a given String into a Double integer.
	 *
	 * @param param String
	 * @return double 0.0 if given String is not a number
	 */
	public static double atof(final String param) {
		double value = 0.0D;
		if (!isStringNullOrEmpty(param)) {
			try {
				value = Double.parseDouble(param);
				// Fix the value
				if (Double.isNaN(value)) {
					value = 0.0D;
				}
			} catch (NumberFormatException ignore) {
				value = 0.0D;
			}
		}
		return value;
	}

	/**
	 * Create a BufferedReader.
	 *
	 * @param szPath String path or null
	 * @param fileName String filename
	 *
	 * @return BufferedReader instance
	 *
	 * @throws FileNotFoundException If any error at File I/O level
	 * @throws IllegalArgumentException If given fileName is null or trim empty
	 */
	public static BufferedReader createFileReader(final String szPath,
		final String fileName
	) throws FileNotFoundException {

		File file;
		FileReader fr;
		Helper.checkStringNullOrEmpty(fileName, "fileName");
		if (Helper.isStringNullOrEmpty(szPath)) {
			file = new File(fileName);

		} else {
			file = new File(szPath + "/" + fileName);
		}
		fr = new FileReader(file);
		return new BufferedReader(fr);
	}

	/**
	 * Helper to close the BufferReader instance.
	 *
	 * @param br BufferReader
	 */
	public static void closeReader(BufferedReader br) {
		try {
			if (br != null) {
			br.close();
			}
		} catch (IOException ignore) {
		}
	}

	/**
	 * Helper to create instance of FileWriter for writing CSV.
	 *
	 * @param pFile File or null
	 * @param fileName String
	 * @param method String 
	 * @return FileWriter instance
	 *
	 * @throws MergeCsvException If there is any error at File I/O level
	 * @throws IllegalArgumentException If given fileName is null or trim empty
	 */
	public static FileWriter createFileWriter(File pFile,
		final String fileName,
		final String method
	) throws MergeCsvException {

		FileWriter fw;
		File file = pFile;
		Helper.checkStringNullOrEmpty(fileName, "fileName");
		if (pFile == null) {
			file = new File(fileName);
		}
		if (file.exists()) {
			if (!file.delete()) {
				throw new MergeCsvException(method + ": Can't delete "
					+ fileName);
			}
		}
		try {
			file.createNewFile();
		} catch (IOException ex) {
			throw new MergeCsvException(method + ": " + ex.getMessage(), ex);
		}
		fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException ex) {
			throw new MergeCsvException(method + ": " + ex.getMessage(), ex);
		}
		return fw;
	}

	/**
	 * Helper to create BufferedWriter for creating CSV file.
	 *
	 * @param fileName String file name
	 * @param method String method
	 * @return BufferedWriter instance
	 *
	 * @throws MergeCsvException If there is any error at File I/O level
	 * @throws IllegalArgumentException If given fileName is null or trim empty
	 */
	public static BufferedWriter createBufferedWriter(final String fileName,
		final String method
	) throws MergeCsvException {
		FileWriter fw;
		BufferedWriter bw;

		fw = Helper.createFileWriter(null, fileName, method);
		bw = new BufferedWriter(fw, BUFFERED_BUFFER_SIZE);

		return bw;
	}

	/**
	 * Helper to close the BufferedWriter instance.
	 *
	 * @param bw BufferedWriter
	 */
	public static void closeWriter(BufferedWriter bw) {
		try {
			if (bw != null) {
			bw.close();
			}
		} catch (IOException ignore) {
		}
	}

	/**
	 * Default constructor.
	 */
	private Helper() {
		// Hidden to prevent creation of Helper.
	}

}
