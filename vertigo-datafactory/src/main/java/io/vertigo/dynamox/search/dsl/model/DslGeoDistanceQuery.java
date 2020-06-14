/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, Vertigo.io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.dynamox.search.dsl.model;

import io.vertigo.core.lang.Assertion;

/**
 * Relative geo distance.
 * #(geoPointFieldName)#~(distance)(distanceUnit)
 * @author npiedeloup
 */
public final class DslGeoDistanceQuery implements DslQuery {
	private final DslQuery geoPoint;
	private final int distance;
	private final String distanceUnit;

	/**
	 * @param geoPoint DslQuery
	 * @param distance distance
	 * @param distanceUnit distance unit
	 */
	public DslGeoDistanceQuery(final DslQuery geoPoint, final int distance, final String distanceUnit) {
		Assertion.check()
				.notNull(geoPoint)
				.argNotEmpty(distanceUnit)
				.argument(geoPoint instanceof DslGeoPointCriteria || geoPoint instanceof DslGeoPointFixed, "DslGeoDistanceQuery only support Criteria or Fixed geoPoint ({0})", geoPoint);
		//-----
		this.geoPoint = geoPoint;
		this.distance = distance;
		this.distanceUnit = distanceUnit;
	}

	/**
	 * @return geoPoint
	 */
	public DslQuery getGeoPoint() {
		return geoPoint;
	}

	/**
	 * @return distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @return distanceUnit
	 */
	public String getDistanceUnit() {
		return distanceUnit;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return geoPoint + "~" + distance + distanceUnit;
	}
}
