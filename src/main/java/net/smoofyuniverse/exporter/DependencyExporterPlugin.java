/*
 * Copyright (c) 2021-2023 Hugo Dupanloup (Yeregorix)
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

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.Directory;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class DependencyExporterPlugin implements Plugin<Project> {
	public static final String USER_AGENT = "DependencyExporter";

	@Override
	public void apply(Project project) {
		NamedDomainObjectContainer<ExportConfig> configs = project.getObjects().domainObjectContainer(ExportConfig.class);
		project.getExtensions().add("dependencyExport", configs);

		Provider<Directory> outputDir = project.getLayout().getBuildDirectory().dir("generated/dependencyExport");

		TaskProvider<ExportTask> exportDeps = project.getTasks().register("exportDependencies", ExportTask.class, task -> {
			task.getConfigurations().addAll(configs);
			task.getOutputDirectory().set(outputDir);
		});

		project.getPlugins().withType(JavaPlugin.class, plugin -> {
			project.getExtensions().getByType(SourceSetContainer.class).named(SourceSet.MAIN_SOURCE_SET_NAME, sourceSet -> {
				sourceSet.getResources().srcDir(exportDeps.map(Task::getOutputs));
			});
		});
	}
}
