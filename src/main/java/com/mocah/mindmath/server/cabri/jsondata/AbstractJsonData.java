/**
 *
 */
package com.mocah.mindmath.server.cabri.jsondata;

import java.lang.reflect.Field;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public abstract class AbstractJsonData {
	/**
	 * Get a field value from this object
	 *
	 * @param fieldName the name of the field
	 * @return the {@code Object} value for the specified field
	 * @throws NoSuchFieldException if a field with the specified name is not found.
	 * @throws NullPointerException If {@code fieldName} is {@code null}
	 * @throws SecurityException    If a security manager, <i>s</i>, is present and
	 *                              any of the following conditions is met:
	 *
	 *                              <ul>
	 *
	 *                              <li>the caller's class loader is not the same as
	 *                              the class loader of this class and invocation of
	 *                              {@link SecurityManager#checkPermission
	 *                              s.checkPermission} method with
	 *                              {@code RuntimePermission("accessDeclaredMembers")}
	 *                              denies access to the declared field
	 *
	 *                              <li>the caller's class loader is not the same as
	 *                              or an ancestor of the class loader for the
	 *                              current class and invocation of
	 *                              {@link SecurityManager#checkPackageAccess
	 *                              s.checkPackageAccess()} denies access to the
	 *                              package of this class
	 *
	 *                              </ul>
	 */
	public Object getFieldValue(String fieldName) throws NoSuchFieldException, SecurityException {
		Class<?> c = getClass();

		Field f = c.getDeclaredField(fieldName);
		f.setAccessible(true);

		String value = null;
		try {
			value = (String) f.get(this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Bloc catch généré automatiquement
			// Note:
			// IllegalArgumentException & IllegalAccessException shouldn't be thrown since
			// we always access to an existing class and fieldn owned by the class itself
			e.printStackTrace();
		}

		return value;
	}
}
