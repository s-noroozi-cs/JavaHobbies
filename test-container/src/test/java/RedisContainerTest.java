import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class RedisContainerTest {
    private static final String redisDockerImage = "redis:6.2.6";

    @Container
    private static GenericContainer redisContainer =
            new GenericContainer(DockerImageName.parse(redisDockerImage))
                    .withExposedPorts(6379);


    @Test
    void checkRedisContainer()throws Exception{
        Assertions.assertNotNull(redisContainer);
        Assertions.assertNotNull(redisContainer.getContainerId());
    }




}
