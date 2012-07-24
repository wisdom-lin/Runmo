import java.io.File;
import java.util.regex.Pattern;


public class Test {
	public static void main(String[] args) {
		int a = 0x01;
		int b = 0x02;
		int c= 0x04;
		int m = 11;
		
		System.out.println(Integer.toBinaryString(m & b));
	}
}
