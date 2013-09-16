package emovie.recommender.runner;

import java.io.File;
import org.kohsuke.args4j.Option;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 5/09/13
 * Time: 0:38
 * To change this template use File | Settings | File Templates.
 */
public class ServerArguments
{
    @Option(name = "-h")
    public String mysqlHost = "localhost";
    @Option(name = "-p")
    public int mysqlPort = 3306;
    @Option(name = "-u")
    public String mysqlUser = "root";
    @Option(name = "-P")
    public String mysqlPassword = "";
    @Option(name = "-d")
    public String mysqlDatabase = "test";

    @Option(name = "-f")
    public File file = null;

    @Option(name = "-gh")
    public String gearmanHost = "localhost";

    @Option(name = "-gp")
    public int gearmanPort = 4730;
}
