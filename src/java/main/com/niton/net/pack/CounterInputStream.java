package com.niton.net.pack;

import java.io.IOException;
import java.io.InputStream;

/**
 * A forwarding stream. Counts the read bytes 
 */
public class CounterInputStream extends InputStream {
	private InputStream out;
	private long mass;
	
	/**
	 * @param out The stream to delegate the reads to
	 */
	public CounterInputStream(InputStream out) {
		super();
		this.out = out;
	}
	
	/**
	 * @return the ammount of bytes read from the underlaying stream
	 */
	public long getRecivedBytes() {
		return mass;
	}

	@Override
	public int read() throws IOException {
		int i = out.read();
		mass++;
		return i;
	}
}
