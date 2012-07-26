import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Arrays;

public class Test {
	public static void main(String[] args) throws Exception {
		int size = 2000;
		byte[] bs = new byte[size];
		byte bb = 6;
		Arrays.fill(bs, bb);
		int[] ins = new int[size / 4];
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(bs)));
		for (int i = 0; i < size / 4; i++) {
			ins[i] = dis.readInt();
		}
		System.out.println(Arrays.toString(bs));
		System.out.println(Arrays.toString(ins));
		dis.close();
	}
}
