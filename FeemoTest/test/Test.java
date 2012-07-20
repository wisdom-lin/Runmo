
public class Test {
	public static void main(String[] args) {
		byte b = -1;
		short sh = (short) (b &0xff);
		System.out.println(sh);
	}
}
