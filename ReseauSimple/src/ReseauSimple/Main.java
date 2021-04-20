package reseausimple;
public class Main {

	public static void main (String[] args) {
		
		int nb;
		int length;
		
		try {
			nb = Integer.parseInt(args[0]);
			length  = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: ./cliser_os nb length");			
			return;
		}
		
		System.out.println("Pretty Simple Model ");
		Init gen = new Init(nb, length , 750, 500);
	}
}
