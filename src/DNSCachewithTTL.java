import java.util.*;

public class DNSCachewithTTL {

    // Entry class
    class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttl) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + ttl * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    // Cache with LRU behavior
    private LinkedHashMap<String, DNSEntry> cache;

    private int capacity = 5;
    private int hits = 0;
    private int misses = 0;

    public DNSCachewithTTL() {

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > capacity;
            }
        };
    }

    // Simulated upstream DNS query
    private String queryUpstream(String domain) {

        // Fake IP generation for demo
        return "172.217.14." + new Random().nextInt(255);
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        DNSEntry entry = cache.get(domain);

        if (entry != null) {

            if (!entry.isExpired()) {
                hits++;
                return "Cache HIT → " + entry.ipAddress;
            }
            else {
                cache.remove(domain);
            }
        }

        // Cache miss
        misses++;

        String ip = queryUpstream(domain);

        DNSEntry newEntry = new DNSEntry(domain, ip, 10);
        cache.put(domain, newEntry);

        return "Cache MISS → Query upstream → " + ip + " (TTL:10s)";
    }

    // Remove expired entries
    public void cleanExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> entry = it.next();

            if (entry.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws Exception {

        DNSCachewithTTL dns = new DNSCachewithTTL();

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(11000);

        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();
    }
}
