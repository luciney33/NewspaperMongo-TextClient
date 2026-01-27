package ui;


import domain.error.AppError;
import domain.model.CredentialDTO;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.Scanner;

public class mainMenu {
    public static void main(String[] args) {

        //siempre hay que inicializar con el try
        try {
            SeContainerInitializer initializer = SeContainerInitializer.newInstance();
            SeContainer container = initializer.initialize();
            CredentialUI credentialUI = container.select(CredentialUI.class).get();
            ArticleUI articleUI = container.select(ArticleUI.class).get();
            NewspaperUI newspaperUI = container.select(NewspaperUI.class).get();
            ReaderUI readerUI = container.select(ReaderUI.class).get();
//            ReadArticleUI readActUI = container.select(ReadArticleUI.class).get();
//            TypeUI typeUI = container.select(TypeUI.class).get();



            Scanner sc = new Scanner(System.in);
            boolean loggedIN = false;
            while (!loggedIN) {
                System.out.println("=========  USUARIOS  =================\n"+
                        "username: u1, password: 1 \n" +
                        "username: u2, password: 1 \n");
                System.out.println("Username");
                String username = sc.nextLine().trim();
                if (username.isEmpty()) continue;

                System.out.println("Password");
                String password = sc.nextLine().trim();
                if (password.isEmpty()) continue;

                loggedIN = credentialUI.checkLogin(new CredentialDTO(username, password, 0));
                if (!loggedIN) {
                    System.out.println("Invalid username");
                } else System.out.println("Valid username");
            }
            int opc;
            do {
                System.out.println("1. Get all articles");
                System.out.println("2. Add article");
                System.out.println("3. Update article");
                System.out.println("4. Delete article");
                System.out.println("5. Get all newspapers");
                System.out.println("6. Get all readers");
                System.out.println("7. Get readers of an article");
                System.out.println("8. Get reader by id");
                System.out.println("9. Add rating to an article");
                System.out.println("10. Modify rating of an article");
                System.out.println("11. Delete rating of an article");
                System.out.println("12. Get all types");
                System.out.println("13. Add a new reader with credentials");
                System.out.println("14. Delete reader");
                System.out.println("15. Exit");
                System.out.print("Select an option: ");
                try {
                    opc = sc.nextInt();
                    sc.nextLine();
                } catch (NumberFormatException e) {
                    opc = -1;
                }

                switch (opc) {
                    case 1:
                        articleUI.getArticles();
                        break;
                    case 2:
//                        articleUI.saveArticle();
                        break;
                    case 3:
//                        articleUI.updateArticle();
                        break;
                    case 4:
//                        articleUI.deleteArticle();
                        break;
                    case 5:
                        newspaperUI.getNewspapers();
                        break;
                    case 6:
                        readerUI.getReaders();
                        break;
                    case 7:
//                        readerUI.getReadersByArticle();
                        break;
                    case 8:
//                        readerUI.getReaderById();
                        break;
                    case 9:
//                        articleUI.addRating();
                        break;
                    case 10:
//                        articleUI.modifyRating();
                        break;
                    case 11:
//                        articleUI.deleteRating();
                        break;
                    case 12:
                        newspaperUI.getNewspapers();
                        break;
//                    case 13:
//                        readerUI.addNewReaderCredentials();
//                        break;
                    case 14:
//                        readerUI.deleteReader();
                        break;
                    case 15:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }

            } while (opc != 15);

        } catch (AppError e) {
            System.err.println("Error : " + e.getMessage());
            System.exit(1);
        }
    }
}
