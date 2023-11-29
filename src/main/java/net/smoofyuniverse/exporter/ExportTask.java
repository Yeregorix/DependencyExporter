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

import com.google.gson.stream.JsonWriter;
import org.gradle.api.DefaultTask;
import org.gradle.api.DomainObjectCollection;
import org.gradle.api.NamedDomainObjectCollection;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.component.ModuleComponentIdentifier;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.internal.artifacts.repositories.resolver.MavenUniqueSnapshotComponentIdentifier;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ExportTask extends DefaultTask {
	private static final Logger logger = Logging.getLogger(ExportTask.class);

	public static void generate(Iterable<ArtifactRepository> repos, Iterable<ResolvedArtifact> artifacts, Path destination,
								String digestAlgorithm, boolean writeDigestAlgorithm, NamedDomainObjectCollection<Constraint> constraints) throws Exception {
		List<String> mavenRepos = new ArrayList<>();
		for (ArtifactRepository repo : repos) {
			if (repo instanceof MavenArtifactRepository) {
				String url = ((MavenArtifactRepository) repo).getUrl().toString();
				if (!url.endsWith("/"))
					url += "/";
				mavenRepos.add(url);
			}
		}

		Files.createDirectories(destination.getParent());

		try (JsonWriter w = new JsonWriter(Files.newBufferedWriter(destination))) {
			w.setIndent("  ");
			w.beginArray();

			for (ResolvedArtifact a : artifacts) {
				ComponentIdentifier id = a.getId().getComponentIdentifier();
				if (!(id instanceof ModuleComponentIdentifier))
					continue;

				ModuleComponentIdentifier c = (ModuleComponentIdentifier) id;

				String name = c.toString();
				String path = c.getGroup().replace('.', '/') + "/" + c.getModule() + "/" + c.getVersion() + "/" + c.getModule() + "-";

				if (c instanceof MavenUniqueSnapshotComponentIdentifier) {
					path += ((MavenUniqueSnapshotComponentIdentifier) c).getTimestampedVersion();
				} else {
					path += c.getVersion();
				}

				String classifier = a.getClassifier();
				if (classifier != null) {
					name += ":" + classifier;
					path += "-" + classifier;
				}

				path += "." + a.getExtension();

				w.beginObject();
				w.name("name");
				w.value(name);

				boolean found = false;
				for (String repoUrl : mavenRepos) {
					String urlStr = repoUrl + path;

					URL url = new URL(urlStr);
					URLConnection co = url.openConnection();
					co.setRequestProperty("User-Agent", DependencyExporterPlugin.USER_AGENT);

					try (InputStream st = co.getInputStream()) {
						if (st != null) {
							found = true;
							w.name("url");
							w.value(urlStr);
							break;
						}
					} catch (FileNotFoundException ignored) {
					}
				}

				if (!found) {
					logger.warn("Artifact URL not found.");
					logger.warn("Name: {}", name);
					logger.warn("Path: {}", path);
				}

				Path file = a.getFile().toPath();
				w.name("size");
				w.value(Files.size(file));
				w.name("digest");
				w.value(Util.toHexString(Util.digest(file, digestAlgorithm)));

				if (writeDigestAlgorithm) {
					w.name("digestAlgorithm");
					w.value(digestAlgorithm);
				}

				if (classifier != null) {
					Constraint constraint = constraints.findByName(classifier);
					if (constraint != null) {
						List<String> systems = constraint.getSystems().getOrNull();
						if (systems != null) {
							w.name("systems");
							w.beginArray();
							for (String system : systems)
								w.value(system);
							w.endArray();
						}

						List<String> archs = constraint.getArchs().getOrNull();
						if (archs != null) {
							w.name("archs");
							w.beginArray();
							for (String arch : archs)
								w.value(arch);
							w.endArray();
						}
					}
				}

				w.endObject();
			}

			w.endArray();
		}
	}

	@OutputDirectory
	public abstract DirectoryProperty getOutputDirectory();

	@Nested
	public abstract DomainObjectCollection<ExportConfig> getConfigurations();

	@TaskAction
	public void generate() throws Exception {
		Path dir = getOutputDirectory().get().getAsFile().toPath();
		RepositoryHandler repos = getProject().getRepositories();

		for (ExportConfig config : getConfigurations()) {
			Set<String> exclusions = config.getExclusions().get();
			List<ResolvedArtifact> artifacts = new ArrayList<>();
			Path destination = dir.resolve(config.getPath().get());

			for (ResolvedArtifact a : config.getConfig().get().getResolvedConfiguration().getResolvedArtifacts()) {
				if (!exclusions.contains(a.getClassifier()))
					artifacts.add(a);
			}

			if (artifacts.isEmpty() && config.getSkipWhenEmpty().get())
				Files.deleteIfExists(destination);
			else
				generate(repos, artifacts, destination, config.getDigestAlgorithm().get(),
						config.getWriteDigestAlgorithm().get(), config.getConstraints());
		}
	}
}
