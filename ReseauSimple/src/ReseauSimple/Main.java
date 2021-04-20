package clientserver;
public class Main {

	public static void main (String[] args) {
		
		String NamePlace;
		String NameTrans;
		
		try {
			NamePlace = args[0];
			NameTrans  = args[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: ./cliser_os NamePlace NameTrans");			
			return;
		}
		
		System.out.println("Model of one Arc ");
		Init gen = new Init(NamePlace, NameTrans , 750, 500);
	}
}
