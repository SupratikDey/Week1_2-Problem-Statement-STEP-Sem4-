import java.util.*;

public class DistributedRateLimiterforAPIGateway {

    // Token Bucket class
    class TokenBucket {

        int tokens;
        int maxTokens;
        long lastRefillTime;

        TokenBucket(int maxTokens) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens every hour
        void refill() {

            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - lastRefillTime;

            if (elapsed >= 3600000) { // 1 hour
                tokens = maxTokens;
                lastRefillTime = currentTime;
            }
        }

        boolean allowRequest() {

            refill();

            if (tokens > 0) {
                tokens--;
                return true;
            }

            return false;
        }
    }

    // clientId -> token bucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int limit = 1000;

    // Check rate limit
    public synchronized String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(limit));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" + bucket.tokens + " requests remaining)";
        }
        else {

            long retry = (3600000 - (System.currentTimeMillis() - bucket.lastRefillTime)) / 1000;

            return "Denied (0 requests remaining, retry after " + retry + "s)";
        }
    }

    // Get rate limit status
    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println("{used: " + used +
                ", limit: " + bucket.maxTokens +
                ", reset_in_seconds: " +
                ((3600000 - (System.currentTimeMillis() - bucket.lastRefillTime)) / 1000) + "}");
    }

    public static void main(String[] args) {

        DistributedRateLimiterforAPIGateway limiter =
                new DistributedRateLimiterforAPIGateway();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        limiter.getRateLimitStatus("abc123");
    }
}