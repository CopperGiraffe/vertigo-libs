/*
 * vertigo - application development platform
 *
 * Copyright (C) 2013-2024, Vertigo.io, team@vertigo.io
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
package io.vertigo.vega.webservice.data.domain;

import com.google.gson.Gson;

import io.vertigo.core.lang.BasicType;
import io.vertigo.core.lang.BasicTypeAdapter;

public class GeoPointJsonAdapter implements BasicTypeAdapter<GeoPointJson, String> {

	private static final Gson GSON = new Gson();

	@Override
	public GeoPointJson toJava(final String geoPointAsJson, final Class<GeoPointJson> type) {
		return GSON.fromJson(geoPointAsJson, GeoPointJson.class);
	}

	@Override
	public String toBasic(final GeoPointJson geopoint) {
		return GSON.toJson(geopoint);
	}

	@Override
	public BasicType getBasicType() {
		return BasicType.String;
	}

}
