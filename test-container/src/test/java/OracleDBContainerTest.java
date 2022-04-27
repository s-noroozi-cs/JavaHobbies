import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.OracleContainer;

@Disabled
public class OracleDBContainerTest {
    //issue related to fixed oracle container in M1 arch.
    private static final String oracleDockerImage = "gvenzl/oracle-xe:21.3.0-slim";
    private static final String oracleSysPass = "sys";

    @ClassRule
    private static final OracleContainer oracleContainer = new OracleContainer(oracleDockerImage)
            .withEnv("ORACLE_PASSWORD",oracleSysPass);

    @BeforeAll
    static void startUp(){
        oracleContainer.start();
    }

    @Test
    void checkOracleContainer() {
        Assertions.assertNotNull(oracleContainer);
        LoggerFactory.getLogger(this.getClass()).info(oracleContainer.getJdbcUrl());
    }
}
