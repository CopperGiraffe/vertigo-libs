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
package io.vertigo.ui;

import java.io.IOException;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Handler that adds the multipart config to the request that passes through if
 * it is a multipart request.
 *
 * <p>
 * Jetty will only clean up the temp files generated by
 * request is about to die but won't invoke it for a non-servlet (webapp)
 * handled request.
 *
 * <p>
 * MultipartConfigInjectionHandler ensures that the parts are deleted after the
 * {@link #handle(String, Request, HttpServletRequest, HttpServletResponse)}
 * method is called.
 *
 * <p>
 * Ensure that no other handlers sit above this handler which may wish to do
 * something with the multipart parts, as the saved parts will be deleted on the return
 * from
 * {@link #handle(String, Request, HttpServletRequest, HttpServletResponse)}.
 */
public class MultipartConfigInjectionHandler extends HandlerWrapper {
	private static final Logger LOG = LogManager.getLogger(MultipartConfigInjectionHandler.class);
	private static final String MULTIPART_FORMDATA_TYPE = "multipart/form-data";
	private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement(
			System.getProperty("java.io.tmpdir"));

	public static boolean isMultipartRequest(final ServletRequest request) {
		return request.getContentType() != null
				&& request.getContentType().startsWith(MULTIPART_FORMDATA_TYPE);
	}

	/**
	 * If you want to have multipart support in your handler, call this method each time
	 * your doHandle method is called (prior to calling getParameter).
	 *
	 * Servlet 3.0 include support for Multipart data with its
	 * {@link HttpServletRequest#getPart(String)} & {@link HttpServletRequest#getParts()}
	 * methods, but the spec says that before you can use getPart, you must have specified a
	 * {@link MultipartConfigElement} for the Servlet.
	 *
	 * <p>
	 * This is normally done through the use of the MultipartConfig annotation of the
	 * servlet in question, however these annotations will not work when specified on
	 * Handlers.
	 *
	 * <p>
	 * The workaround for enabling Multipart support in handlers is to define the
	 * MultipartConfig attribute for the request which in turn will be read out in the
	 * getPart method.
	 *
	 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=395000#c0">Jetty Bug
	 *      tracker - Jetty annotation scanning problem (servlet workaround) </a>
	 * @see <a href="http://dev.eclipse.org/mhonarc/lists/jetty-users/msg03294.html">Jetty
	 *      users mailing list post.</a>
	 */
	public static void enableMultipartSupport(final HttpServletRequest request) {
		request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
	}

	@Override
	public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException, ServletException {
		final boolean multipartRequest = HttpMethod.POST.is(request.getMethod())
				&& isMultipartRequest(request);
		if (multipartRequest) {
			enableMultipartSupport(request);
		}

		try {
			super.handle(target, baseRequest, request, response);
		} finally {
			if (multipartRequest) {
				Collection<Part> multiParts;
				try {
					multiParts = request.getParts();
					if (multiParts != null && !multiParts.isEmpty()) {
						for (final Part part : multiParts) {
							try {
								// a multipart request to a servlet will have the parts cleaned up correctly, but
								// the repeated call to deleteParts() here will safely do nothing.
								part.delete();
							} catch (final IOException e) {
								LOG.warn("Error while deleting multipart request parts", e);
							}
						}
					}
				} catch (IOException | ServletException errorParts) {
					LOG.warn("Error while deleting multipart request parts", errorParts);
				}
			}
		}
	}
}
