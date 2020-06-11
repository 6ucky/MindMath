package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Task implements Serializable {

	private static final long serialVersionUID = 4790322015762458488L;

	@Id
	private final String id;

	private final String task;

	private final String trigger;

	@OneToOne(cascade = CascadeType.ALL)
	private final Sensors sensors;

	@OneToOne(cascade = CascadeType.ALL)
	private final Params params;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<Log> log;

	// empty object
	public Task() {
		List<Log> emptylist = new ArrayList<>();
		this.log = emptylist;
		this.params = new Params("");
		this.trigger = null;
		this.task = null;
		this.sensors = new Sensors("");
		this.id = "";
	}

	public Task(String id, String task, String trigger, Sensors sensors, Params params, List<Log> log) {
		this.id = id;
		this.task = task;
		this.trigger = trigger;
		this.sensors = sensors;
		this.params = params;
		this.log = log;
	}

	public String getId() {
		return id;
	}

	public String getTask() {
		return task;
	}

	public String getTrigger() {
		return trigger;
	}

	public Sensors getSensors() {
		return sensors;
	}

	public Params getParams() {
		return params;
	}

	public List<Log> getLog() {
		return log;
	}

	/**
	 * Get a field value from this Task object
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
//		f.setAccessible(true);

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
