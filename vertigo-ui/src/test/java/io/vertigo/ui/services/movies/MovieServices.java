/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.ui.services.movies;

import java.util.List;
import java.util.Optional;

import io.vertigo.core.component.Component;
import io.vertigo.dynamo.collections.model.FacetedQueryResult;
import io.vertigo.dynamo.collections.model.SelectedFacetValues;
import io.vertigo.dynamo.domain.model.DtList;
import io.vertigo.dynamo.domain.model.DtListState;
import io.vertigo.dynamo.search.model.SearchQuery;
import io.vertigo.ui.domain.movies.Movie;
import io.vertigo.ui.domain.movies.MovieDisplay;
import io.vertigo.ui.domain.movies.MovieIndex;

public interface MovieServices extends Component {

	DtList<Movie> getMovies(DtListState dtListState);

	void save(Movie movie);

	Movie get(Long movId);

	DtList<MovieDisplay> getMoviesDisplay(DtListState dtListState);

	DtList<MovieIndex> getMovieIndex(List<Long> movieIds);

	FacetedQueryResult<MovieIndex, SearchQuery> searchMovies(String criteria, SelectedFacetValues listFilters, DtListState dtListState, Optional<String> group);
}
