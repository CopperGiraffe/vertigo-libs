package io.vertigo.rest.impl.rest.servlet;

import org.apache.log4j.Logger;

/**
 * Impl�mentation du listener des �v�nements produits par la servlet.
 * @author pchretien
 */
final class ServletListener {

	/**
	 * M�canisme de log racine
	 */
	private final Logger generalLog;

	/**
	 * Constructeur.
	 */
	ServletListener() {
		generalLog = Logger.getRootLogger();
	}

	// --------------------------------------------------------------------------

	/**
	 * Ev�nement remont� lors du d�marrage de la servlet.
	 * @param servletName Nom de la servlet
	 */
	public void onServletStart(final String servletName) {
		if (generalLog.isInfoEnabled()) {
			generalLog.info("Start servlet " + servletName);
		}
	}

	/**
	 * Ev�nement remont� lors de l'arr�t de la servlet.
	 * @param servletName Nom de la servlet
	 */
	public void onServletDestroy(final String servletName) {
		if (generalLog.isInfoEnabled()) {
			generalLog.info("Destroy servlet " + servletName);
		}
	}
}
