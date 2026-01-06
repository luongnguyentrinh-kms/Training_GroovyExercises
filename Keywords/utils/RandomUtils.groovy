package utils

public class RandomUtils {

    static long randomId() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits())
    }

}
