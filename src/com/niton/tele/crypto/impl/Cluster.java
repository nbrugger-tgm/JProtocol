package com.niton.tele.crypto.impl;

public class Cluster {
	private byte[][] cluster;

	public Cluster(byte[] data) {
		double sqrt = Math.sqrt(data.length);
		if (sqrt == (long) sqrt) {
			cluster = new byte[(int) sqrt][(int) sqrt];
			setData(data);
		} else
			throw new IllegalArgumentException("Data has to be an byte[n²] Value");
	}

	public Cluster(byte[][] cluster) {
		this.cluster = cluster;
	}

	public Cluster(int size) {
		cluster = new byte[size][size];
	}

	public Cluster(int size, byte fill) {
		this(size);
		for (int i = 0; i < cluster.length; i++)
			for (int j = 0; j < cluster[i].length; j++)
				cluster[i][j] = fill;
	}

	public Cluster(int size, byte[] data) {
		this(size);
		setData(data);
	}

	public byte get(int col, int row) {
		return cluster[row][col];
	}

	public byte[] getByteArray() {
		int size = 0;
		for (byte[] element : cluster)
			size += element.length;
		byte[] array = new byte[size];
		int m = 0;
		for (byte[] sub : cluster) {
			for (byte element : sub) {
				array[m] = element;
				m++;
			}
		}
		return array;
	}

	public byte[][] getCluster() {
		return cluster;
	}

	public void pushColum(int col, int push) {
		Cluster original = new Cluster(getByteArray());
		for (int j = 0; j < cluster.length; j++) {
			byte my = original.get(col, j);
			int moveToIndex = j + push;
			moveToIndex %= cluster.length;
			while (moveToIndex < 0)
				moveToIndex += cluster.length;
			set(col, moveToIndex, my);
		}
	}

	public void pushRow(int row, int push) {
		byte[] rowA = cluster[row];
		byte[] newRow = new byte[rowA.length];
		for (int i = 0; i < newRow.length; i++) {
			int newIndex = i + push;
			newIndex %= newRow.length;
			while (newIndex < 0)
				newIndex += rowA.length;
			newRow[newIndex] = rowA[i];
		}
		cluster[row] = newRow;
	}

	public byte set(int col, int row, byte value) {
		return cluster[row][col] = value;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String line = "\n";
		for (byte[] bs : cluster) {
			for (byte b : bs) {
				builder.append('[');
				if (b < 10 && b > -10) {
					builder.append(' ');
					builder.append(' ');
				} else if (b < 100 && b > -100)
					builder.append(' ');
				builder.append(b);
				builder.append(']');
			}
			builder.append(line);
		}
		return builder.toString();
	}
}
