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

/**
 * <p>
 * Thrown if there is any error.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong>The exceptions are not thread safe.
 * </p>
 *
 * @author Teddy Albon Sr &lt;tmalbonph@yahoo.com&gt;
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MergeCsvException extends Exception {
	/**
	 * <p>
	 * Constructor with message parameter.
	 * </p>
	 * @param message the exception message.
	 */
	public MergeCsvException(String message) {
		super(message);
	}

	/**
	 * <p>
	 * Constructor with message and cause parameter.
	 * </p>
	 * @param message the exception message.
	 * @param cause the exception cause.
	 */
	public MergeCsvException(String message, Throwable cause) {
		super(message, cause);
	}
}
