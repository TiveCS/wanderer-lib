package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.menu.events.ComponentClickEvent;
import io.github.tivecs.wanderer.menu.events.ComponentPostRenderEvent;
import io.github.tivecs.wanderer.menu.events.ComponentPreRenderEvent;
import io.github.tivecs.wanderer.menu.events.ComponentStateUpdateEvent;

public interface MenuComponentInteraction {

    default void onClick(ComponentClickEvent event){}
    default void onStateUpdate(ComponentStateUpdateEvent event){}
    default void onPreRender(ComponentPreRenderEvent event){}
    default void onPostRender(ComponentPostRenderEvent event){}

}
