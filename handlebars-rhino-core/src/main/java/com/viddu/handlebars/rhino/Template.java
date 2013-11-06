package com.viddu.handlebars.rhino;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Template implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -9213689689189770811L;

    private static final String HANDLEBARS_RUNTIME_LIB = "/handlebars.runtime-v1.1.2.js";
    private static final String RENDER_TEMPLATE = "var template = Handlebars.templates['template']; template(context);";
    private final Scriptable renderScope;
    private final String compiledTemplate;

    Logger logger = LoggerFactory.getLogger(Template.class);

    public Template(String compiledTemplate) throws IOException {
        this.compiledTemplate = compiledTemplate;
        Context context = Context.enter();
        renderScope = context.initStandardObjects();
        String handlerbarRuntimeLib = IOUtils.toString(this.getClass().getResourceAsStream(HANDLEBARS_RUNTIME_LIB));
        context.evaluateString(renderScope, handlerbarRuntimeLib, "hbr_runtime", 1, null);
        context.evaluateString(renderScope, compiledTemplate, "renderJS", 1, null);
        Context.exit();
    }

    public <T> String apply(Class<T> clazz, T contextMap) throws IntrospectionException {
        NativeObject handlebarContext = new NativeObject();
        BeanInfo info = Introspector.getBeanInfo(clazz, Object.class);
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            if (pd.getReadMethod() != null) {
                try {
                    handlebarContext.defineProperty(pd.getName(), pd.getReadMethod().invoke(contextMap),
                            NativeObject.READONLY);
                } catch (IllegalArgumentException e) {
                    logger.error("Exception while accessing bean properties: ", e);
                } catch (IllegalAccessException e) {
                    logger.error("Exception while accessing bean properties: ", e);
                } catch (InvocationTargetException e) {
                    logger.error("Exception while accessing bean properties: ", e);
                }
            }
        }
        return render(handlebarContext);
    }

    public String apply(Map<String, Object> contextMap) {
        NativeObject handlebarContext = new NativeObject();
        for (Map.Entry<String, Object> entry : contextMap.entrySet()) {
            handlebarContext.defineProperty(entry.getKey(), entry.getValue(), NativeObject.READONLY);
        }
        return render(handlebarContext);
    }

    protected String render(NativeObject handlebarContext) {
        Context context = Context.enter();
        renderScope.put("context", renderScope, handlebarContext);
        Object result = context.evaluateString(renderScope, RENDER_TEMPLATE, "somefile", 1, null);
        String html = Context.toString(result);
        Context.exit();
        logger.debug("Successfully rendered template:\n{}", html);
        return html;
    }

    public void apply(Map<String, Object> contextMap, Writer writer) throws IOException {
        writer.write(apply(contextMap));
    }

    @Override
    public String toString() {
        return compiledTemplate;
    }

}
