package com.niton.net.pack;

import java.io.IOException;
import java.io.OutputStream;
/**
 * This stream counts the written bytes
 */
public class CounterOutputStream extends OutputStream {
	private OutputStream out;
	private long mass;
	
	/**
	 * @param out the stream to forward to
	 */
	public CounterOutputStream(OutputStream out) {
		super();
		this.out = out;
	}
	/**
	 * @return the number of bytes written to the underlaying socket
	 */
	public long getSendBytes() {
		return mass;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
		mass++;
	}
}
