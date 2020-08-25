package io.vertigo.database.impl.migration;

import io.vertigo.core.node.component.Plugin;

/**
 * A DataBaseMigrationPlugin is dedicated to a specific database connection.
 * It is responsible to performs tasks on that specific connection.
 * @author mlaroche
 *
 */
public interface MigrationPlugin extends Plugin {

	/**
	 * @return the name of the connection the plugin is for
	 */
	String getConnectionName();

	/** {@see MigrationManager} */
	void update();

	/** {@see MigrationManager} */
	void check();

}
