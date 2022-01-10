package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.menu.events.ComponentClickEvent;
import io.github.tivecs.wanderer.menu.events.ComponentPostRenderEvent;
import io.github.tivecs.wanderer.menu.events.ComponentPreRenderEvent;

public interface MenuComponentInteraction {

    void onClick(ComponentClickEvent event);
    void onPreRender(ComponentPreRenderEvent event);
    void onPostRender(ComponentPostRenderEvent event);

}
