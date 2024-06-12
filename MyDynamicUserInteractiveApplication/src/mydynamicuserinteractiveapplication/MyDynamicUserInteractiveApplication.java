package mydynamicuserinteractiveapplication;

import java.util.Scanner;

public class MyDynamicUserInteractiveApplication {

    public static void main(String[] args) {
        DBConnection dbconn = new DBConnection();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("You want to Register / Login / 'exit': ");
            String option = scanner.nextLine();
            
            if (option.equalsIgnoreCase("exit")) {
                          break;
                  }
            
            if (option.equalsIgnoreCase("register")){
                registerUser(dbconn, scanner);
            } else if (option.equalsIgnoreCase("login")){
                loginUser(dbconn, scanner);
            } else{
                System.out.println("Invalid option. Please try again!!");
            }
        }    

        System.out.print("Do you want to print the data? (yes/no): ");
        String printOption = scanner.nextLine();
        if (printOption.equalsIgnoreCase("yes")) {
            printData(dbconn);
        }

        scanner.close();
    }
    
        private static void registerUser(DBConnection dbconn , Scanner scanner) { 
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();

            while (!dbconn.isUsernameUnique(username)) {
                System.out.println("Username already taken. Please enter a different username: ");
                username = scanner.nextLine();
            }

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            dbconn.registerUser(username, password);
            System.out.println("User registered successfully!!");
        }
        
        
        private static void loginUser(DBConnection dbconn , Scanner scanner) { 
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            
            if (!dbconn.isUsernameExists(username)){
                System.out.println("Username doesn't exit. Error!!  Please try again!");
                return;
            } 

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

          if (dbconn.authenticateUser(username, password)){
            System.out.println("Login Successfull!");
        } else {
    System.out.println("Inavlid Username or Password . Please try again!");
    
   }       
 }     

    private static void printData(DBConnection dbconn) {
        dbconn.printAllUsers();
    }
}