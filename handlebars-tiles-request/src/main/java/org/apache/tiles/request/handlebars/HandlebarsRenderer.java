package org.apache.tiles.request.handlebars;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.Renderer;

import com.viddu.handlebars.rhino.Handlebars;
import com.viddu.handlebars.rhino.Template;

public class HandlebarsRenderer implements Renderer {

    private Pattern acceptPattern;

    private String prefix;
    private String suffix;
    private Map<String, Template> templateMap = new HashMap<String, Template>();

    HandlebarsRenderer(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public void render(String templateName, Request request) throws IOException {
        // Compile template after reading file from file system.
        String templatePath = new StringBuilder(prefix).append("/").append(templateName).append(suffix).toString();
        if (!templateMap.containsKey(templateName))
            templateMap.put(templateName, Handlebars.compile(getFileContent(templatePath)));

        // Render template
        Template compiledTemplate = templateMap.get(templateName);
        compiledTemplate.apply(buildScope(request), request.getWriter());
    }

    private String getFileContent(String fileName) {
        try {
            InputStream is = this.getClass().getResourceAsStream(fileName);
            return IOUtils.toString(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isRenderable(String path, Request request) {
        if (path == null) {
            return false;
        }
        if (acceptPattern != null) {
            final Matcher matcher = acceptPattern.matcher(path);
            return matcher.matches();
        }
        return true;
    }

    public final void setAcceptPattern(Pattern acceptPattern) {
        this.acceptPattern = acceptPattern;
    }

    public Pattern getAcceptPattern() {
        return acceptPattern;
    }

    protected Map<String, Object> buildScope(Request request) {
        Map<String, Object> scope = new HashMap<String, Object>();
        List<String> availableScopes = request.getAvailableScopes();
        for (int i = availableScopes.size() - 1; i >= 0; --i) {
            scope.putAll(request.getContext(availableScopes.get(i)));
        }
        return scope;
    }
}
