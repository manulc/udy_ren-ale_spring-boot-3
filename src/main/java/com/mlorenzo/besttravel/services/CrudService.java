package com.mlorenzo.besttravel.services;

public interface CrudService<RQ, RS, ID> {
    RS getById(ID id);
    RS create(RQ request);
    void delete(ID id);

}
