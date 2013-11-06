package org.apache.tiles.request.handlebars;

import java.util.regex.Pattern;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.extras.complete.CompleteAutoloadTilesContainerFactory;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.render.BasicRendererFactory;
import org.apache.tiles.request.render.ChainedDelegateRenderer;
import org.apache.tiles.request.render.Renderer;

public class HandlebarsTilesContainerFactory extends CompleteAutoloadTilesContainerFactory {

    private static final String HANDLEBARS = "handlebars";
    private final HandlebarsRenderer handlebarsRenderer;

    public HandlebarsTilesContainerFactory(String prefix, String suffix) {
        this.handlebarsRenderer = new HandlebarsRenderer(prefix, suffix);//new HandlebarsRenderer(prefix, suffix);
    }

    @Override
    protected void registerAttributeRenderers(BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext, TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        super.registerAttributeRenderers(rendererFactory, applicationContext, container, attributeEvaluatorFactory);
        handlebarsRenderer.setAcceptPattern(Pattern.compile(".*.hbr"));
        rendererFactory.registerRenderer(HANDLEBARS, handlebarsRenderer);
    }

    @Override
    protected Renderer createDefaultAttributeRenderer(BasicRendererFactory rendererFactory,
            ApplicationContext applicationContext, TilesContainer container,
            AttributeEvaluatorFactory attributeEvaluatorFactory) {
        ChainedDelegateRenderer retValue = (ChainedDelegateRenderer) super.createDefaultAttributeRenderer(
                rendererFactory, applicationContext, container, attributeEvaluatorFactory);
        retValue.addAttributeRenderer(rendererFactory.getRenderer(HANDLEBARS));
        return retValue;
    }
}
