import java.util.*;
public class SocialMediaUsernameAvailabilityChecker {
    private HashMap<String, Integer> users = new HashMap<>();

    private HashMap<String, Integer> attempts = new HashMap<>();

    public SocialMediaUsernameAvailabilityChecker() {
        users.put("john_doe", 101);
        users.put("admin", 102);
        users.put("user123", 103);
    }

    public boolean checkAvailability(String username) {

        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !users.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            String suggestion = username + i;

            if (!users.containsKey(suggestion))
                suggestions.add(suggestion);
        }

        String alt = username.replace("_", ".");

        if (!users.containsKey(alt))
            suggestions.add(alt);

        return suggestions;
    }

    public String getMostAttempted() {

        String most = null;
        int max = 0;

        for (String user : attempts.keySet()) {

            if (attempts.get(user) > max) {
                max = attempts.get(user);
                most = user;
            }
        }

        return most + " (" + max + " attempts)";
    }

    public void registerUser(String username, int id) {

        if (checkAvailability(username))
            users.put(username, id);
        else
            System.out.println("Username already taken.");
    }

    public static void main(String[] args) {

        SocialMediaUsernameAvailabilityChecker system = new SocialMediaUsernameAvailabilityChecker();

        System.out.println(system.checkAvailability("john_doe"));
        System.out.println(system.checkAvailability("jane_smith"));

        System.out.println(system.suggestAlternatives("john_doe"));

        System.out.println(system.getMostAttempted());
    }
}
