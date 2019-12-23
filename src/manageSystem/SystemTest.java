package manageSystem;

import java.util.Scanner;

public class SystemTest {
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		systemUtils utl = new systemUtils();
		
		while(true) {
			System.out.println("------WELCOME------\n1. Register\n2. Login\n3. End");
			int choice = input.nextInt();
			if(choice == 1) utl.register();
			
			else if(choice ==2) {
				if(utl.login()) break;
			}
			
			else {
				System.out.println("Goodbye~");
				System.exit(0);
			}
		}
		
		utl.shopCart();
		input.close();
	}
}
