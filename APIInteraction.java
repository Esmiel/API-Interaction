import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Scanner;

public class APIInteraction {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> viewAllPosts();
                case 2 -> viewPost(scanner);
                case 3 -> createPost(scanner);
                case 4 -> updatePost(scanner);
                case 5 -> deletePost(scanner);
                case 6 -> {
                    System.out.println("Exiting program...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- API Interaction Menu ---");
        System.out.println("1. View All Posts");
        System.out.println("2. View a Specific Post");
        System.out.println("3. Create a New Post");
        System.out.println("4. Update an Existing Post");
        System.out.println("5. Delete a Post");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void viewAllPosts() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("GET Response:");
        System.out.println(response.body());
    }

    private static void viewPost(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Enter the ID of the post to view: ");
        int postId = scanner.nextInt();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + postId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("GET Response:");
        System.out.println(response.body());
    }

    private static void createPost(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter body: ");
        String body = scanner.nextLine();

        String json = String.format("""
                {
                    "title": "%s",
                    "body": "%s",
                    "userId": 1
                }
                """, title, body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("POST Response:");
        System.out.println(response.body());
    }

    private static void updatePost(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Enter the ID of the post to update: ");
        int postId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter new title: ");
        String title = scanner.nextLine();

        System.out.print("Enter new body: ");
        String body = scanner.nextLine();

        String json = String.format("""
                {
                    "id": %d,
                    "title": "%s",
                    "body": "%s",
                    "userId": 1
                }
                """, postId, title, body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + postId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("PUT Response:");
        System.out.println(response.body());
    }

    private static void deletePost(Scanner scanner) throws IOException, InterruptedException {
        System.out.print("Enter the ID of the post to delete: ");
        int postId = scanner.nextInt();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + postId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("DELETE Response:");
        System.out.println("Response code: " + response.statusCode());
    }
}
