/*
 * The JUnit-addons Software License, Version 1.0
 *     (based on the Apache Software License, Version 1.1)
 *
 * Copyright (c) 2002-2003 Vladimir R. Bossicard.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by Vladimir R.
 *        Bossicard as well as other contributors
 *        (http://junit-addons.sourceforge.net/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The name "JUnit-addons" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact
 *    vbossica@users.sourceforge.net.
 *
 * 5. Products derived from this software may not be called "JUnit-addons"
 *    nor may "JUnit-addons" appear in their names without prior written
 *    permission of the project managers.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ======================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals.  For more information on the JUnit-addons Project, please
 * see <http://junit-addons.sourceforge.net/>.
 */

package com.butlerpress.cyclinglog;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

public class DateAssert {

	private DateAssert() {
	}
	
	public static void assertEqualsToMonth(String message, Date expected, Date actual) {
		if (checkForNullEquality(message, expected, actual)) {
			return;
		}
		Calendar calExpected = Calendar.getInstance();
		calExpected.setTime(expected);
		Calendar calActual = Calendar.getInstance();
		calActual.setTime(actual);
		assertEqualsToMonth(message, calExpected, calActual);
	}
	
	public static void assertEqualsToMonth(String message, Calendar expected, Calendar actual) {
		if (checkForNullEquality(message, expected, actual)) {
			return;
		}
		Assert.assertEquals(message + " (year is wrong", 
			expected.get(Calendar.YEAR),
			actual.get(Calendar.YEAR));
		Assert.assertEquals(message + " (month is wrong", 
			expected.get(Calendar.MONTH),
			actual.get(Calendar.MONTH));
	}
	
	/**
	 * Assert that two dates have the same year, month, and day
	 */
	public static void assertEqualsToDay(String message, Calendar expected, Calendar actual) {
		if (checkForNullEquality(message, expected, actual)) {
			return;
		}
		Assert.assertEquals(message + " (year is wrong", 
			expected.get(Calendar.YEAR),
			actual.get(Calendar.YEAR));
		Assert.assertEquals(message + " (month is wrong", 
			expected.get(Calendar.MONTH),
			actual.get(Calendar.MONTH));
		Assert.assertEquals(message + " (day of month is wrong", 
			expected.get(Calendar.DAY_OF_MONTH),
			actual.get(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * Assert that two dates have the same year, month, and day
	 */
	public static void assertEqualsToDay(String message, Date expected, Date actual) {
		if (checkForNullEquality(message, expected, actual)) {
			return;
		}
		Calendar calExpected = Calendar.getInstance();
		calExpected.setTime(expected);
		Calendar calActual = Calendar.getInstance();
		calActual.setTime(actual);
		assertEqualsToDay(message, calExpected, calActual);
	}
	
	/**
	 * Assert that two dates have the same year, month, and day
	 */
	public static void assertEqualsToDay(String message, Timestamp expected, Date actual) {
		if (checkForNullEquality(message, expected, actual)) {
			return;
		}
		Calendar calExpected = Calendar.getInstance();
		calExpected.setTimeInMillis(expected.getTime());
		Calendar calActual = Calendar.getInstance();
		calActual.setTime(actual);
		assertEqualsToDay(message, calExpected, calActual);
	}

	/**
	 * Assert that two dates have the same year, month, and day
	 */
	public static void assertEqualsToDay(Timestamp expected, Date actual) {
		assertEqualsToDay("", expected, actual);
	}

	public static void assertEqualsToSecond(String message, Date expected, Date actual) {
		if (checkForNullEquality(message, expected, actual)) {
			return;
		}
		Calendar calExpected = Calendar.getInstance();
		calExpected.setTime(expected);
		calExpected.set(Calendar.MILLISECOND, 0);

		Calendar calActual = Calendar.getInstance();
		calActual.setTime(actual);
		calActual.set(Calendar.MILLISECOND, 0);
		
		if (!calExpected.equals(calActual)) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			message = message + " <" + format.format(expected) + "> not equal to <" +
				format.format(actual) + ">";
			Assert.fail(message);
		}
	}
	
	private static boolean checkForNullEquality(String message, Object expected, Object actual) {
		if (expected == null && actual == null) {
			return true;
		}
		if ((expected == null && actual != null) || (expected != null && actual == null)) {
			Assert.fail(message + " <" + expected + "> not equal to <" + actual + ">");
		}
		return false;
	}
}
