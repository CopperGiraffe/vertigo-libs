package ${packageName};


/**
 * Attention cette classe est g�n�r�e automatiquement !
 */
public enum ${classSimpleName} {

<#list roles as role>
	/**
	 * ${role.name}.
	 */
	${role.name},
</#list>
}
