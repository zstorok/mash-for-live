package hu.zstorok.mashforlive.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for serving the Ember.js frontend.
 * 
 * @author cfstras
 */
@RestController
public class EmberController {
	private final boolean debug = true;
	private final Path basePath = Paths.get("public/");

	@RequestMapping(value = "/index.html")
	public void index(HttpServletResponse res) throws IOException {
		Path f = Paths.get(basePath.toString(), "index.html");

		if (debug) {
			StringBuilder b = new StringBuilder();
			getJavascripts().forEach(
					(i) -> b.append("<script src=\""
							+ basePath.relativize(i).toString()
									.replace('\\', '/')
							+ "\"></script>"));
			String i = b.toString();
			b.setLength(0);
			Files.lines(f).forEachOrdered((l) -> {
				b.append(l.replace("<script src=\"/js/all.js\"></script>", i));
			});
			res.getWriter().append(b.toString());
		} else {
			Files.copy(f, res.getOutputStream());
		}
	}

	@RequestMapping(value = "/js/templates.js", produces = "application/javascript; charset=utf-8")
	public void templates(HttpServletResponse res) throws IOException {
		Path ts = basePath.resolve("templates/");
		Files.find(
				ts,
				10,
				(path, attr) -> path.toString().endsWith(".hbs")
						&& attr.isRegularFile())
				.forEach(
						(f) -> {
							try {
								String name = ts.relativize(f).toString()
										.replace('\\', '/')
										.replaceFirst("\\.hbs$", "");
								String templ = StringEscapeUtils
										.escapeEcmaScript(FileUtils
												.readFileToString(f.toFile()));
								res.getWriter()
										.append("Ember.TEMPLATES['" + name
												+ "'] = ")
										.append("Ember.Handlebars.compile(\""
												+ templ + "\");");
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
	}

	@RequestMapping(value = "/js/all.js", produces = "application/javascript; charset=utf-8")
	public void javascripts(HttpServletResponse res) throws IOException {
		Path ts = basePath.resolve("js/");
		getJavascripts().forEach(
				(f) -> {
					try {
						res.getWriter().append("// ")
								.append(ts.relativize(f).toString())
								.append("\n");
						IOUtils.copy(Files.newBufferedReader(f),
								res.getWriter());
						res.getWriter().append("\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
	}

	private Stream<Path> getJavascripts() throws IOException {
		Path ts = basePath.resolve("js/");
		return Files.find(ts,
				10, // TODO sort files when accumulating
				(path, attr) -> path.toString().endsWith(".js")
						&& attr.isRegularFile()
						&& !path.startsWith(basePath.resolve("js/libs"))
						&& !path.startsWith(basePath.resolve("js/app.js")));
	}
}