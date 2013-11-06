package com.viddu.handlebars.rhino;

import static org.junit.Assert.assertEquals;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class HandlebarsRhinoTest {

    @Test
    public void testCompileRenderTemplateMapContext() throws IOException, IntrospectionException {
        Template template = Handlebars.compile("{{greeting}},{{name}}");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "Viddu");
        context.put("greeting", "Hiya");
        String html = template.apply(context);
        assertEquals("Hiya,Viddu", html);
    }

    @Test
    public void testCompileRenderTemplateGenericContext() throws IOException, IntrospectionException {
        Template template = Handlebars.compile("{{greeting}},{{name}}");
        Greeting greet = new Greeting("Hiya", "Viddu");
        String html = template.apply(Greeting.class, greet);
        assertEquals("Hiya,Viddu", html);
    }
}
