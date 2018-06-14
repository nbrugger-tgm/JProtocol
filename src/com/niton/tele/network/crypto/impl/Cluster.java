package com.niton.tele.network.crypto.impl;

public class Cluster {
	private byte[][] cluster;

	public Cluster(int size) {
		cluster = new byte[size][size];
	}

	public Cluster(int size, byte fill) {
		this(size);
		for (int i = 0; i < cluster.length; i++) {
			for (int j = 0; j < cluster[i].length; j++) {
				cluster[i][j] = fill;
			}
		}
	}

	public Cluster(int size, byte[] data) {
		this(size);
		setData(data);
	}

	public Cluster(byte[] data) {
		double sqrt = Math.sqrt(data.length);
		if (sqrt == (long) sqrt) {
			cluster = new byte[(int) sqrt][(int) sqrt];
			setData(data);
		} else {
			throw new IllegalArgumentException("Data has to be an byte[n²] Value");
		}
	}

	public Cluster(byte[][] cluster) {
		this.cluster = cluster;
	}

	public void pushColum(int col, int push) {
		Cluster original = new Cluster(getByteArray());
		for (int j = 0; j < cluster.length; j++) {
			byte my = original.get(col, j);
			int moveToIndex = j + push;
			moveToIndex %= cluster.length;
			while(moveToIndex < 0)
				moveToIndex += cluster.length;
			set(col, moveToIndex, (byte) (my/*+push*/));
		}
	}

	public void pushRow(int row, int push) {
		byte[] rowA = cluster[row];
		byte[] newRow = new byte[rowA.length];
		for (int i = 0; i < newRow.length; i++) {
			int newIndex = i + push;
			newIndex %= newRow.length;
			while(newIndex < 0) {
				newIndex += rowA.length;
			}
			newRow[newIndex] = (byte) (rowA[i]/*+push*/);
		}
		cluster[row] = newRow;
	}

	public void setData(byte[] data) {
		int c = 0;
		for (int i = 0; i < cluster.length; i++) {
			byte[] b = cluster[i];
			for (int j = 0; j < b.length; j++) {
				cluster[i][j] = data[c];
				c++;
			}
		}
	}

	public byte get(int col, int row) {
		return cluster[row][col];
	}

	public byte set(int col, int row, byte value) {
		return cluster[row][col] = value;
	}

	public byte[][] getCluster() {
		return cluster;
	}

	public byte[] getByteArray() {
		int size = 0;
		for (int i = 0; i < cluster.length; i++) {
			size += cluster[i].length;
		}
		byte[] array = new byte[size];
		int m = 0;
		for (int i = 0; i < cluster.length; i++) {
			byte[] sub = cluster[i];
			for (int j = 0; j < sub.length; j++) {
				array[m] = sub[j];
				m++;
			}
		}
		return array;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String line = "\n";
		for (int i = 0; i < cluster.length; i++) {
			byte[] bs = cluster[i];
			for (int j = 0; j < bs.length; j++) {
				byte b = bs[j];
				builder.append('[');
				if (b < 10 && b > -10) {
					builder.append(' ');
					builder.append(' ');
				} else if (b < 100&& b > -100) {

					builder.append(' ');
				}
				builder.append(b);
				builder.append(']');
			}
			builder.append(line);
		}
		return builder.toString();
	}
}
