import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.google.gson.*;

public class Main {

    public static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Which data structure? A BST or LinkedList? 1 or 2");
        int option = 0;
        while (option > 2 || option < 1) {
            try {
                option = kb.nextInt();
                if (option > 2 || option < 1) System.out.println("Invalid option, please try again");
            } catch (InputMismatchException e) {
                System.out.println("Invalid option, please try again");
            }
            kb.nextLine();
        }

            switch (option) {
                case 1:
                    useRedBlack();
                    break;
                case 2:
                    useLL();
                    break;
            }

    }

    public static void useRedBlack() {
        System.out.println("Welcome to the Lichess Leaderboard Extractor using RBT");
        System.out.println("Please choose which leaderboard you would like to access");

        boolean success = false;

        int topNum = 0;
        String content = "";
        String gamemode = "";

        while (!success) {
            boolean correctInt = false;
            System.out.println("ultraBullet, bullet, blitz, rapid, " +
                    "classical, chess960, crazyhouse, antichess, " +
                    "atomic, horde, kingOfTheHill, racingKings, threeCheck");
            gamemode = kb.next();
            while (!correctInt) {
                System.out.println("Please enter the top X number of players you would like.");
                try {
                    topNum = kb.nextInt();
                    correctInt = true;
                } catch (InputMismatchException e) {
                    System.out.println("Enter a number bro");
                }
                kb.nextLine();
            }
            try {
                content = callLichess(gamemode, topNum);
                success = true;
            } catch (Exception e) {
                System.out.println("Make sure you spell the name correctly!");
            }
           // kb.nextLine();
        }


        redBlack rbt = new redBlack();


        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(content);
        JsonObject jsonObject = jsonTree.getAsJsonObject();
        JsonElement f1 = jsonObject.get("users");
        JsonArray inside = f1.getAsJsonArray();

        // parse out the information I want, such as Username and Rating
        for (int i = 0; i < topNum; i++) {
            JsonObject in = inside.get(i).getAsJsonObject();
            String username = in.get("username").getAsString();
            int rating = in.get("perfs").getAsJsonObject().
                    get(gamemode).getAsJsonObject().
                    getAsJsonObject().get("rating").
                    getAsJsonPrimitive().getAsInt();

            // parsed out info adding to data structure
            System.out.println(username + ": " + rating);
            rbt.add(username, rating);
        }


        boolean cont = true;
        while (cont) {
            System.out.println("a- You can add a player, f - find a player, remove, or p - print out the list, press q to stop");
            String choice = kb.next();
            switch (choice) {
                case "a":
                    System.out.println("Enter their username");
                    String name = kb.next();
                    System.out.println("Enter rating");
                    int rating = kb.nextInt();
                    rbt.add(name, rating);
                    break;
                case "r":
                    System.out.println("Who would you like to remove?");
                    String toRemove = kb.next();
                    if (rbt.find(toRemove) != null) {
                        rbt.remove(toRemove);
                    }
                    break;
                case "f":
                    System.out.println("Enter their username");
                    String name1 = kb.next();
                    Node n = rbt.find(name1);
                    if (n != null) {
                        System.out.println(n.username + " was found! Their rating is: " + n.rating);
                    } else
                        System.out.println(name1 + "was not found");
                    break;
                case "p":
                    redBlack.inOrderTraversal(rbt.root);
                    break;
                case "q":
                    cont = false;
                    System.out.println("byebye!");
                    break;
            }
        }
    }

    public static void useLL() {
        System.out.println("Welcome to the Lichess Leaderboard Extractor using LL");
        System.out.println("Please choose which leaderboard you would like to access");


        boolean success = false;
        int topNum = 0;
        String content = "";
        String gamemode = "";

        while (!success) {
            boolean correctInt = false;
            System.out.println("ultraBullet, bullet, blitz, rapid, " +
                    "classical, chess960, crazyhouse, antichess, " +
                    "atomic, horde, kingOfTheHill, racingKings, threeCheck");
            gamemode = kb.next();
            while (!correctInt) {
                System.out.println("Please enter the top X number of players you would like.");
                try {
                    topNum = kb.nextInt();
                    correctInt = true;
                } catch (InputMismatchException e) {
                    System.out.println("Enter a number bro");
                }
                kb.nextLine();
            }
            try {
                content = callLichess(gamemode, topNum);
                success = true;
            } catch (Exception e) {
                System.out.println("Make sure you spell the name correctly!");
            }
            // kb.nextLine();
        }

        LinkedList list = new LinkedList();


        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(content);
        JsonObject jsonObject = jsonTree.getAsJsonObject();

        JsonElement f1 = jsonObject.get("users");
        JsonArray inside = f1.getAsJsonArray();

        // parse out the information I want, such as Username and Rating
        for (int i = 0; i < topNum; i++) {
            JsonObject in = inside.get(i).getAsJsonObject();
            String username = in.get("username").getAsString();
            int rating = in.get("perfs").getAsJsonObject().
                    get(gamemode).getAsJsonObject().
                    getAsJsonObject().get("rating").
                    getAsJsonPrimitive().getAsInt();

            // parsed out info adding to data structure
            System.out.println(username + ": " + rating);
            Node n = new Node(username, rating);
            list.add(n);
        }
        boolean cont = true;
        while (cont) {
            System.out.println("a- You can add a player, f - find a player,r - remove a player by username, or p - print out the list, press q to stop");
            String choice = kb.next();
            switch (choice) {
                case "a":
                    System.out.println("Enter their username");
                    String name = kb.next();
                    System.out.println("Enter rating");
                    int rating = kb.nextInt();
                    Node n = new Node(name, rating);
                    list.add(n);
                    break;
                case "r":
                    System.out.println("Who would you like to remove?");
                    String toRemove = kb.next();
                    if (list.find(toRemove) != null) {
                        list.remove(toRemove);
                    }
                    break;
                case "f":
                    System.out.println("Enter their username");
                    String name1 = kb.next();
                    Node n1 = list.find(name1);
                    if (n1 != null) {
                        System.out.println(n1.username + " was found! Their rating is: " + n1.rating);
                    } else
                        System.out.println(name1 + "was not found");
                    break;
                case "p":
                    list.printList();
                    break;
                case "q":
                    cont = false;
                    System.out.println("byebye!");
                    break;
            }
        }

    }

    public static String callLichess(String mode, int topNum) throws Exception   {
        String content = "";

                URL url = new URL("https://www.lichess.org/player/top/" + topNum + "/" + mode);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // For a GET request
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.lichess.v3+json");
                connection.setRequestProperty("Content-Type", "application/vnd.lichess.v3+json");
                String contentType = connection.getHeaderField("Content-Type");
                // To store our response

                // Get the input stream of the connection
                try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;

                    while ((line = input.readLine()) != null) {
                        content = content + line;
                    }
                } finally {
                    connection.disconnect();
                }

        return content;
    }

} // main
