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
package io.vertigo.audit.impl.ledger;

import java.math.BigInteger;

import io.vertigo.audit.ledger.LedgerAddress;
import io.vertigo.core.node.component.Plugin;

/**
 * @author xdurand
 *
 */
public interface LedgerPlugin extends Plugin {

	/**
	 * Sent data to the default address on the ledger.
	 * @param data data in clear to store
	 */
	void sendData(String data);

	/**
	 * Gets the current balance of the provided address
	 * @param ledgerAddress
	 * @return the current balance of the provided address
	 */
	BigInteger getWalletBalance(LedgerAddress ledgerAddress);

	/**
	 * Get the current balance of the wallet
	 * @return the current balance of the wallet
	 */
	BigInteger getMyWalletBalance();

}
