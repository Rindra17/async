package hei.school.async.endpoint.event.consumer.model;

import hei.school.async.PojaGenerated;
import hei.school.async.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
