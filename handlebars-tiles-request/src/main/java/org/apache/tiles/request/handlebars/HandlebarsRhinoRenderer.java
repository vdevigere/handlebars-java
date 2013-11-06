package org.apache.tiles.request.handlebars;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.tiles.request.Request;

import com.viddu.handlebars.rhino.Handlebars;
import com.viddu.handlebars.rhino.Template;

public class HandlebarsRhinoRenderer extends HandlebarsRenderer {

    private String prefix;
    private String suffix;
    private Map<String, Template> templateMap = new HashMap<String, Template>();

    HandlebarsRhinoRenderer(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public void render(String templateName, Request request) throws IOException {
        // Compile template after reading file from file system.
        String templatePath = new StringBuilder(prefix).append("/").append(templateName).append(suffix).toString();
        if (!templateMap.containsKey(templateName))
            templateMap.put(templateName, Handlebars.compile(getFileContent(templatePath)));

        //Render template
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
}
