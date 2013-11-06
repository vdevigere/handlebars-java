package org.apache.tiles.request.handlebars;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.Renderer;

public abstract class HandlebarsRenderer implements Renderer {

    private Pattern acceptPattern;

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
