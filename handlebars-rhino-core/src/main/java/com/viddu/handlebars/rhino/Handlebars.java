package com.viddu.handlebars.rhino;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Handlebars {
    private static final Scriptable compileScope;
    private static final String HANDLEBARSJS_LIB = "/handlebars-v1.1.2.js";
    private static final String COMPILE_TEMPLATE = "Handlebars.precompile(template);";

    private static final Logger logger = LoggerFactory.getLogger(Handlebars.class);
    static {
        Context context = Context.enter();
        compileScope = context.initStandardObjects();
        try {
            String handlerbarLib = IOUtils.toString(Handlebars.class.getResourceAsStream(HANDLEBARSJS_LIB));
            context.evaluateString(compileScope, handlerbarLib, "hbr_full", 1, null);
        } catch (IOException e) {
            logger.error("Exception while reading {} file.", HANDLEBARSJS_LIB, e);
        }
        Context.exit();
    }

    public static Template compile(File file) throws FileNotFoundException, IOException {
        String template = IOUtils.toString(new FileInputStream(file));
        return compile(template);
    }

    public static Template compile(InputStream is) throws IOException {
        String template = IOUtils.toString(is);
        return compile(template);
    }

    public static Template compile(String template) throws IOException {
        Context context = Context.enter();
        compileScope.put("template", compileScope, template);
        String preCompiledTemplate = Context.toString(context.evaluateString(compileScope, COMPILE_TEMPLATE,
                "preCompileJS", 1, null));
        String compiledTemplate = wrap(preCompiledTemplate);
        Context.exit();
        logger.debug("Successfully compiled template:\n {}", compiledTemplate);
        return new Template(compiledTemplate);
    }

    private static String wrap(String preCompiledTemplate) {
        StringBuilder strb = new StringBuilder(
                "(function(){\n  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};\n  templates[\'template\'] = template(");
        strb.append(preCompiledTemplate).append(");\n}());");
        return strb.toString();
    }

}
