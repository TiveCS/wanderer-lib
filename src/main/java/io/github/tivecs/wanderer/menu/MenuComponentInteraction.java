package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.menu.events.*;

public interface MenuComponentInteraction {

    default void onClick(ComponentClickEvent event){}
    default void onStateUpdate(ComponentStateUpdateEvent event){}
    default void onComponentCreation(ComponentCreationEvent event){}
    default void onPreRender(ComponentPreRenderEvent event){}
    default void onPostRender(ComponentPostRenderEvent event){}

}
