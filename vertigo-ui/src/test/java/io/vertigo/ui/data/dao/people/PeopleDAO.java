package io.vertigo.ui.data.dao.people;

import javax.inject.Inject;

import io.vertigo.core.lang.Generated;
import io.vertigo.datamodel.smarttype.SmartTypeManager;
import io.vertigo.datamodel.structure.model.UID;
import io.vertigo.datamodel.task.TaskManager;
import io.vertigo.datastore.entitystore.EntityStoreManager;
import io.vertigo.datastore.impl.dao.DAO;
import io.vertigo.datastore.impl.dao.StoreServices;
import io.vertigo.ui.data.domain.people.People;

/**
 * This class is automatically generated.
 * DO NOT EDIT THIS FILE DIRECTLY.
 */
@Generated
public final class PeopleDAO extends DAO<People, java.lang.Long> implements StoreServices {

	/**
	 * Contructeur.
	 * @param entityStoreManager Manager de persistance
	 * @param taskManager Manager de Task
	 */
	@Inject
	public PeopleDAO(final EntityStoreManager entityStoreManager, final TaskManager taskManager, final SmartTypeManager smartTypeManager) {
		super(People.class, entityStoreManager, taskManager, smartTypeManager);
	}

	/**
	 * Indique que le keyConcept associé à cette UID va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement)
	 * @param uid UID du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	public People readOneForUpdate(final UID<People> uid) {
		return entityStoreManager.readOneForUpdate(uid);
	}

	/**
	 * Indique que le keyConcept associé à cet id va être modifié.
	 * Techniquement cela interdit les opérations d'ecriture en concurrence
	 * et envoie un évenement de modification du keyConcept (à la fin de transaction eventuellement)
	 * @param id Clé du keyConcept modifié
	 * @return KeyConcept à modifier
	 */
	public People readOneForUpdate(final java.lang.Long id) {
		return readOneForUpdate(createDtObjectUID(id));
	}
}
