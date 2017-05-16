/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.vertigo.x.account.plugins.authc.ldap;

import java.util.Hashtable;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

import io.vertigo.dynamo.domain.model.URI;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.MessageText;
import io.vertigo.vega.webservice.exception.VSecurityException;
import io.vertigo.x.account.authc.AuthenticationToken;
import io.vertigo.x.account.authc.UsernamePasswordToken;
import io.vertigo.x.account.identity.Account;
import io.vertigo.x.account.impl.authc.AuthenticatingRealmPlugin;

public final class LdapAuthenticatingRealmPlugin implements AuthenticatingRealmPlugin {
	private static final Logger LOGGER = Logger.getLogger(LdapAuthenticatingRealmPlugin.class);

	private static final String DEFAULT_CONTEXT_FACTORY_CLASS_NAME = "com.sun.jndi.ldap.LdapCtxFactory";
	private static final String SIMPLE_AUTHENTICATION_MECHANISM_NAME = "simple";
	private static final String DEFAULT_REFERRAL = "follow";

	private static final String USERDN_SUBSTITUTION_TOKEN = "{0}";
	private String userDnPrefix;
	private String userDnSuffix;
	private final String ldapServer;

	/**
	 * Constructor.
	 * @param userDnTemplate userDnTemplate
	 * @param ldapServerHost Ldap Server host
	 * @param ldapServerPort Ldap server port (default : 389)
	 */
	@Inject
	public LdapAuthenticatingRealmPlugin(@Named("userDnTemplate") final String userDnTemplate,
			@Named("ldapServerHost") final String ldapServerHost, @Named("ldapServerPort") final String ldapServerPort) {
		parseUserDnTemplate(userDnTemplate);
		ldapServer = ldapServerHost + ":" + ldapServerPort;
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

	/** {@inheritDoc} */
	@Override
	public Optional<URI<Account>> authenticateAccount(final AuthenticationToken token) {
		final UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		LdapContext ldapContext = null;
		try {
			ldapContext = createLdapContext(usernamePasswordToken.getUsername(), usernamePasswordToken.getPassword());
			ldapContext.lookup(userDnPrefix + usernamePasswordToken.getUsername() + userDnSuffix);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Ouverture de connexion LDAP  \"" + ldapContext.toString() + "\"");
			}
			return Optional.of(new URI<Account>(DtObjectUtil.findDtDefinition(Account.class), token.getUsername()));
		} catch (final NamingException e) {
			LOGGER.info("Authentification de l'utilisateur " + usernamePasswordToken.getUsername() + " auprès de l'annuaire LDAP : KO", e);
			return Optional.empty();
		} finally {
			closeLdapContext(ldapContext);
		}
	}

	private void parseUserDnTemplate(final String template) {
		Assertion.checkArgNotEmpty(template, "User DN template cannot be null or empty.");
		//----
		final int index = template.indexOf(USERDN_SUBSTITUTION_TOKEN);
		if (index < 0) {
			final String msg = "User DN template must contain the '" +
					USERDN_SUBSTITUTION_TOKEN + "' replacement token to understand where to " +
					"insert the runtime authentication principal.";
			throw new IllegalArgumentException(msg);
		}
		final String prefix = template.substring(0, index);
		final String suffix = template.substring(prefix.length() + USERDN_SUBSTITUTION_TOKEN.length());

		userDnPrefix = prefix;
		userDnSuffix = suffix;
	}

	private LdapContext createLdapContext(final String principal, final String credentials) throws NamingException {
		final Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, DEFAULT_CONTEXT_FACTORY_CLASS_NAME);
		env.put(Context.REFERRAL, DEFAULT_REFERRAL);

		env.put(Context.SECURITY_AUTHENTICATION, SIMPLE_AUTHENTICATION_MECHANISM_NAME);
		final String url = "ldap://" + ldapServer;
		env.put(Context.PROVIDER_URL, url);
		if (credentials != null) {
			env.put(Context.SECURITY_PRINCIPAL, protectLdap(principal));
			env.put(Context.SECURITY_CREDENTIALS, protectLdap(credentials));
		} else {
			env.put(Context.SECURITY_AUTHENTICATION, "none");
		}
		return new InitialLdapContext(env, null);
	}

	private static String protectLdap(final String principal) {
		return EsapiLdapEncoder.encodeForDN(principal);
	}

	private static void closeLdapContext(final LdapContext ldapContext) {
		if (ldapContext != null) {
			try {
				ldapContext.close();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Fermeture connexion Ldap  \"" + ldapContext.toString() + "\" ");
				}
			} catch (final NamingException e) {
				LOGGER.error("Erreur lors de la fermeture de l'objet LdapContext", e);
				throw new VSecurityException(MessageText.of(e.getMessage()));
			}
		}
	}
}
