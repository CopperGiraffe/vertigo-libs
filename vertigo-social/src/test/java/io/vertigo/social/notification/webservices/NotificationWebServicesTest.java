/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.social.notification.webservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.parsing.Parser;
import io.vertigo.account.account.Account;
import io.vertigo.account.account.AccountGroup;
import io.vertigo.account.account.AccountManager;
import io.vertigo.commons.impl.connectors.redis.RedisConnector;
import io.vertigo.core.node.AutoCloseableApp;
import io.vertigo.core.util.InjectorUtil;
import io.vertigo.dynamo.domain.model.UID;
import io.vertigo.social.MyNodeConfig;
import io.vertigo.social.data.MockIdentities;
import io.vertigo.social.services.notification.Notification;
import io.vertigo.social.services.notification.NotificationServices;
import redis.clients.jedis.Jedis;

public final class NotificationWebServicesTest {
	private static final int WS_PORT = 8088;
	private final SessionFilter sessionFilter = new SessionFilter();
	private static AutoCloseableApp app;

	@Inject
	private MockIdentities mockIdentities;
	@Inject
	private AccountManager identityManager;
	@Inject
	private RedisConnector redisConnector;
	@Inject
	private NotificationServices notificationServices;

	@BeforeAll
	public static void setUp() {
		beforeSetUp();
		app = new AutoCloseableApp(MyNodeConfig.vegaConfig());
	}

	@BeforeEach
	public void setUpInstance() {
		InjectorUtil.injectMembers(this);
		//---
		try (final Jedis jedis = redisConnector.getResource()) {
			jedis.flushAll();
		}
		mockIdentities.initData();

		preTestLogin();
	}

	private void preTestLogin() {
		RestAssured.registerParser("plain/text", Parser.TEXT);
		RestAssured.given()
				.filter(sessionFilter)
				.get("/test/login?id=1");
	}

	@AfterEach
	public void purgeNotifications() {
		final UID<Account> accountUID = UID.of(Account.class, "1");
		final List<Notification> notifications = notificationServices.getCurrentNotifications(accountUID);
		for (final Notification notification : notifications) {
			notificationServices.remove(accountUID, notification.getUuid());
		}
	}

	@AfterAll
	public static void tearDown() {
		if (app != null) {
			app.close();
		}
	}

	private static void beforeSetUp() {
		//RestAsssured init
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = WS_PORT;
	}

	@Test
	public void testGetCurrentNotifications() {
		final Notification notification = Notification.builder()
				.withSender("ExtensionTest")
				.withType("MSG")
				.withTitle("Message de Vertigo")
				.withTargetUrl("#keyConcept@2")
				.withContent("Lorem ipsum")
				.build();
		final Set<UID<Account>> accountUIDs = identityManager.getAccountUIDs(UID.of(AccountGroup.class, "100"));
		notificationServices.send(notification, accountUIDs);

		RestAssured.given().filter(sessionFilter)
				.expect()
				.body("size()", Matchers.greaterThanOrEqualTo(1))
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/api/messages");
	}

	@Test
	public void testGetRemoveNotifications() {
		final Notification notification = Notification.builder()
				.withSender("ExtensionTest")
				.withType("MSG")
				.withTitle("Message de Vertigo")
				.withTargetUrl("#keyConcept@2")
				.withContent("Lorem ipsum")
				.build();
		final Set<UID<Account>> accountUIDs = identityManager.getAccountUIDs(UID.of(AccountGroup.class, "100"));
		notificationServices.send(notification, accountUIDs);

		RestAssured.given().filter(sessionFilter)
				.expect()
				.body("size()", Matchers.equalTo(1))
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/api/messages");

		RestAssured.given().filter(sessionFilter)
				.expect()
				.statusCode(HttpStatus.SC_NO_CONTENT)
				.log().ifError()
				.when()
				.delete("/x/notifications/api/messages/" + notification.getUuid().toString());

		RestAssured.given().filter(sessionFilter)
				.expect()
				.body("size()", Matchers.equalTo(0))
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/api/messages");

	}

	@Test
	public void testGetRemoveManyNotifications() {
		final Notification notification1 = Notification.builder()
				.withSender("ExtensionTest")
				.withType("MSG")
				.withTitle("Message de Vertigo")
				.withTargetUrl("#keyConcept@2")
				.withContent("Lorem ipsum")
				.build();
		final Notification notification2 = Notification.builder()
				.withSender("ExtensionTest")
				.withType("MSG")
				.withTitle("Message de Vertigo")
				.withTargetUrl("#keyConcept@2")
				.withContent("Lorem ipsum")
				.build();
		final Set<UID<Account>> accountUIDs = identityManager.getAccountUIDs(UID.of(AccountGroup.class, "100"));
		notificationServices.send(notification1, accountUIDs);
		notificationServices.send(notification2, accountUIDs);

		RestAssured.given().filter(sessionFilter)
				.expect()
				.body("size()", Matchers.equalTo(2))
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/api/messages");

		final List<String> notificationUuids = new ArrayList<>();
		notificationUuids.add(notification1.getUuid().toString());
		notificationUuids.add(notification2.getUuid().toString());

		RestAssured.given().filter(sessionFilter)
				.body(notificationUuids)
				.expect()
				.statusCode(HttpStatus.SC_NO_CONTENT)
				.log().ifError()
				.when()
				.delete("/x/notifications/api/messages");

		RestAssured.given().filter(sessionFilter)
				.expect()
				.body("size()", Matchers.equalTo(0))
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/api/messages");

	}

	@Test
	public void testGetStatus() {
		RestAssured.given()
				.expect()
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/infos/status");
	}

	@Test
	public void testGetStats() {
		RestAssured.given()
				.expect()
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/infos/stats");
	}

	@Test
	public void testGetConfig() {
		RestAssured.given()
				.expect()
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/infos/config");
	}

	@Test
	public void testGetHelp() {
		RestAssured.given()
				.expect()
				.statusCode(HttpStatus.SC_OK)
				.log().ifError()
				.when()
				.get("/x/notifications/infos/help");
	}

}
