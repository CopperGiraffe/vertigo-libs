/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.x.account.authc;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.vertigo.app.AutoCloseableApp;
import io.vertigo.core.component.di.injector.DIInjector;
import io.vertigo.persona.security.UserSession;
import io.vertigo.persona.security.VSecurityManager;
import io.vertigo.x.account.identity.Account;

/**
 * Implementation standard de la gestion centralisee des droits d'acces.
 *
 * @author npiedeloup
 */
public final class AuthentificationManagerTest {

	private AutoCloseableApp app;

	@Inject
	private VSecurityManager securityManager;

	@Inject
	private AuthentificationManager authentificationManager;

	@Before
	public void setUp() {
		app = new AutoCloseableApp(MyAppConfig.config(false));
		DIInjector.injectMembers(this, app.getComponentSpace());
		securityManager.startCurrentUserSession(securityManager.createUserSession());
	}

	@After
	public void tearDown() {
		securityManager.stopCurrentUserSession();
		if (app != null) {
			app.close();
		}
	}

	@Test
	public void testAuthenticateFail() {
		final AuthenticationToken token = new UsernamePasswordToken("badUserName", "badPassword");
		final Optional<Account> account = authentificationManager.authenticate(token);
		Assert.assertFalse("Shouldn't found any account with a bad login", account.isPresent());

		final Optional<UserSession> userSession = securityManager.getCurrentUserSession();
		Assert.assertTrue("No UserSession", userSession.isPresent());
		Assert.assertFalse("Badly authenticated", userSession.get().isAuthenticated());
	}

	@Test
	public void testAuthenticateSuccess() {
		authenticateSuccess();
	}

	private Optional<Account> authenticateSuccess() {
		final AuthenticationToken token = new UsernamePasswordToken("ISIS", "Azerty1!");
		final Optional<Account> account = authentificationManager.authenticate(token);
		Assert.assertTrue("Authent fail", account.isPresent());

		final Optional<UserSession> userSession = securityManager.getCurrentUserSession();
		Assert.assertTrue("No UserSession", userSession.isPresent());
		Assert.assertTrue("Not authenticated", userSession.get().isAuthenticated());
		return account;
	}

	@Test
	public void testLoggedAccount() {
		final Optional<Account> account = authenticateSuccess();

		final Optional<Account> loggedAccount = authentificationManager.getLoggedAccount();

		Assert.assertEquals(account.get(), loggedAccount.get());
	}

	@Test
	public void testLogout() {
		final Optional<Account> account = authenticateSuccess();
		final Optional<UserSession> userSession = securityManager.getCurrentUserSession();
		Assert.assertTrue("No UserSession", userSession.isPresent());
		Assert.assertTrue("Not authenticated", userSession.get().isAuthenticated());

		authentificationManager.logout();

		Assert.assertFalse("Badly authenticated", userSession.get().isAuthenticated());
	}
}
