/**
 *
 */
package io.vertigo.commons.health;

import java.time.Instant;

import io.vertigo.lang.Assertion;

/**
 * Health check.
 *  example :
 *  dataStorePlugin.ping : Ping to a Database produces a specific health check with the actual measure of it
 *  {plugin/component}.{test} :
 *
 * @author mlaroche
 */
public final class HealthCheck {
	private final String name;
	private final String checker;
	private final HealthMeasure healthMeasure;
	private final Instant checkInstant;

	/**
	 * Constructor.
	 *
	 * @param name the health check name
	 * @param checker who  created the measure
	 * @param checkInstant when the check was performed
	 * @param healthMeasure the measure
	 */
	public HealthCheck(
			final String name,
			final String checker,
			final Instant checkInstant,
			final HealthMeasure healthMeasure) {
		Assertion.checkNotNull(name);
		Assertion.checkNotNull(checker);
		Assertion.checkNotNull(checkInstant);
		Assertion.checkNotNull(healthMeasure);
		//-----
		this.name = name;
		this.checker = checker;
		this.checkInstant = checkInstant;
		this.healthMeasure = healthMeasure;
	}

	/**
	 * @return the health check name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the checker
	 */
	public String getChecker() {
		return checker;
	}

	/**
	 * @return the instant when the check was performed
	 */
	public Instant getCheckInstant() {
		return checkInstant;
	}

	/**
	 * @return the measure
	 */
	public HealthMeasure getMeasure() {
		return healthMeasure;
	}

}
