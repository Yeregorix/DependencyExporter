/*
 * Copyright (c) 2021 Hugo Dupanloup (Yeregorix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.smoofyuniverse.exporter;

import org.gradle.api.Named;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Nested;

import javax.inject.Inject;

public class ExportConfig implements Named {
	private final ObjectFactory factory;
	private final String name;
	private final Property<String> path;
	private final Property<Configuration> config;
	private final Property<Boolean> skipWhenEmpty;
	private final NamedDomainObjectContainer<Constraint> constraints;

	@Inject
	public ExportConfig(String name, ObjectFactory factory) {
		this.factory = factory;
		this.name = name;
		this.path = factory.property(String.class);
		this.config = factory.property(Configuration.class);
		this.skipWhenEmpty = factory.property(Boolean.class);
		this.constraints = factory.domainObjectContainer(Constraint.class);

		this.skipWhenEmpty.convention(false);
	}

	@Override
	@Input
	public String getName() {
		return this.name;
	}

	@Input
	public Property<String> getPath() {
		return this.path;
	}

	public void setPath(String value) {
		this.path.set(value);
	}

	@Input
	public Property<Configuration> getConfig() {
		return this.config;
	}

	public void setConfig(Configuration value) {
		this.config.set(value);
	}

	@Input
	public Property<Boolean> getSkipWhenEmpty() {
		return this.skipWhenEmpty;
	}

	public void setSkipWhenEmpty(boolean value) {
		this.skipWhenEmpty.set(value);
	}

	@Nested
	public NamedDomainObjectContainer<Constraint> getConstraints() {
		return this.constraints;
	}

	public void constraint(String name, Iterable<? extends String> systems, Iterable<? extends String> archs) {
		Constraint constraint = new Constraint(name, this.factory);
		constraint.setSystems(systems);
		constraint.setArchs(archs);
		this.constraints.add(constraint);
	}
}
