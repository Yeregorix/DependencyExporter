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
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;

public class Constraint implements Named {
	private final String name;
	private final ListProperty<String> systems;
	private final ListProperty<String> archs;

	@Inject
	public Constraint(String name, ObjectFactory factory) {
		this.name = name;
		this.systems = factory.listProperty(String.class);
		this.archs = factory.listProperty(String.class);
	}

	@Override
	@Input
	public String getName() {
		return this.name;
	}

	@Input
	@Optional
	public ListProperty<String> getSystems() {
		return this.systems;
	}

	public void setSystems(Iterable<? extends String> values) {
		this.systems.set(values);
	}

	@Input
	@Optional
	public ListProperty<String> getArchs() {
		return this.archs;
	}

	public void setArchs(Iterable<? extends String> values) {
		this.archs.set(values);
	}
}
