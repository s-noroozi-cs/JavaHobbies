import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import redis.clients.jedis.Jedis;

@Testcontainers
public class RedisContainerTest {
    private static final String redisDockerImage = "redis:6.2.6";

    @Container
    private static GenericContainer redisContainer =
            new GenericContainer(DockerImageName.parse(redisDockerImage))
                    .withExposedPorts(6379);
    private static Jedis jedis;
    private static final String redisKey= "myKey";
    private static final String redisValue = "myValue";

    @BeforeAll
    static void initRedisClient(){
        jedis = new Jedis(redisContainer.getHost(),redisContainer.getFirstMappedPort());
    }

    @Test
    void checkRedisContainer()throws Exception{
        Assertions.assertNotNull(redisContainer);
        Assertions.assertNotNull(redisContainer.getContainerId());
        Assertions.assertNotNull(jedis);
        Assertions.assertNotNull(jedis.ping());
    }

    @Test
    void writeMessage(){
        Assertions.assertEquals(jedis.set(redisKey,redisValue),"OK");
    }

    @Test
    void readMessage(){
        Assertions.assertEquals(jedis.get(redisKey),redisValue);
    }






}
