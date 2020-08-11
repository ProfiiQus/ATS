package eu.profiiqus.ats.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.profiiqus.ats.ATS;
import eu.profiiqus.ats.managers.ConfigManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Connection pool manager manages connection pool connections to the database.
 *
 * @author Prof
 */
public class ConnectionPoolManager {

    private final ATS plugin;
    private ConfigManager config;
    private HikariDataSource dataSource;

    /**
     * Connection Pool Manager constructor, runs the setup and setupPool method
     * @param plugin Java plugin instance
     */
    public ConnectionPoolManager(ATS plugin) {
        this.plugin = plugin;
        this.config = ConfigManager.getInstance();
        this.setupPool();
    }

    /**
     * SetupPool method sets up all Hikari Connection pool variables.
     * After this method, HikariCP should be fully initialized and ready to rumble.
     */
    private void setupPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(
                "jdbc:mysql://" +
                        this.config.hostname +
                        ":" +
                        this.config.port +
                        "/" +
                        this.config.database
        );
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setUsername(this.config.username);
        hikariConfig.setPassword(this.config.password);
        hikariConfig.setMinimumIdle(this.config.minimumConnections);
        hikariConfig.setMaximumPoolSize(this.config.maximumConnections);
        hikariConfig.setConnectionTimeout(this.config.timeout);
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * GetConnection method returns some of the available connections from
     * the Hikari connection pool.
     * @return MySQL Database connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    /**
     * Close method closes the connection, statement batch or result set
     * @param conn Connection to close
     * @param ps Statement batch to close
     * @param res Result set to close
     */
    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }

    /**
     * Close pool method closes HikariCP, no more connections are generated
     */
    public void closePool() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
        }
    }
}
